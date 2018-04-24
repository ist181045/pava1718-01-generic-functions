/*
 * MIT License
 *
 * Copyright (c) 2018 Francisca Cambra, Rui Ventura
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectiveMagic {

  @SuppressWarnings("unused")
  public static Object invoke(Object receiver, String name, Object... args) {
    Class<?>[] argTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);

    if (!(receiver instanceof Class<?>)) {
      receiver = receiver.getClass();
    }

    try {
      Method method = bestMethod(((Class<?>) receiver), name, argTypes);
      return method.invoke(receiver, args);
    } catch (ReflectiveOperationException roe) {
      //region Print out error message
      String[] argNames = Arrays.stream(args)
          .map(Object::getClass)
          .map(Class::getSimpleName)
          .toArray(String[]::new);

      System.err.println("No applicable method: When calling '"
          + roe.getMessage() + "' with ("
          + String.join(",", argNames) + "), no method is applicable.");
      System.err.println("No restarts available (this isn't as good as CLOS)");
      //endregion
      System.exit(1);
    }

    return null; // Never reached
  }

  private static Method bestMethod(Class<?> type, String name, Class[] argTypes)
      throws NoSuchMethodException {
    return bestMethod(type, name, argTypes.clone(), argTypes);
  }

  private static Method bestMethod(Class<?> type, String name, Class[] origTypes, Class[] argTypes)
      throws NoSuchMethodException {
    try {
      return type.getMethod(name, argTypes);
    } catch (NoSuchMethodException nsme) {
      ///region Check where the last Object is
      int i = argTypes.length - 1;
      while (i > 0 && argTypes[i] == Object.class) {
        argTypes[i] = origTypes[i--]; // reset the current type
      }

      Class<?> current = argTypes[i];
      //endregion
      if (current == Object.class) {
        //region If it's the first, it means a method couldn't be found
        if (i == 0) {
          int dollarPos = name.indexOf('$');
          String simpleName = name.substring(0, dollarPos > 0 ? dollarPos : name.length());
          throw new NoSuchMethodException(type.getSimpleName() + "." + simpleName);
        }
        //endregion
        //region Otherwise, proceed to the previous argument
        argTypes[i] = origTypes[i];
        current = argTypes[--i];
        //endregion
      } else {
        //region Check if it is an array, primitive or wrapper and convert it
        Class<?> converted = current;
        if (current.isArray()) {
          converted = ClassUtils.arraySuperClass(current);
        } else if (current.isPrimitive()) {
          converted = ClassUtils.primitiveToWrapper(current);
        } else if (ClassUtils.isPrimitiveWrapper(current)) {
          converted = ClassUtils.wrapperToPrimitive(current);
        }
        //endregion
        //region If a conversion happened, try getting the method with the converted type
        if (current != converted) {
          argTypes[i] = converted;
          try {
            return type.getMethod(name, argTypes);
          } catch (NoSuchMethodException ignored) {
          }
        }
        //endregion
        //region Otherwise, try crawling the interface hierarchy
        for (Class<?> iface : current.getInterfaces()) {
          argTypes[i] = iface;
          Method method = bestMethod(type, name, origTypes, argTypes);
          if (method != null) {
            return method;
          }
        }
        //endregion
      }
      //region Ultimately, if all else failed, crawl up class hierarchy
      Class<?> parent = current.getSuperclass();

      if (parent != null) {
        argTypes[i] = parent;
        return bestMethod(type, name, origTypes, argTypes);
      } else {
        // To break from interface crawling loop
        argTypes[i] = current;
        return null;
      }
      //endregion
    }
  }
}
