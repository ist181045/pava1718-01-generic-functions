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

  @SuppressWarnings("unchecked")
  public static Object invoke(Object receiver, String name, Object... args) {
    Class<?>[] argTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);

    if (!(receiver instanceof Class<?>)) {
      receiver = receiver.getClass();
    }

    try {
      Method method = bestMethod((Class<?>) receiver, name, argTypes);
      return method.invoke(receiver, args);
    } catch (ReflectiveOperationException roe) {
      throw new RuntimeException(roe);
    }
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
      int i = argTypes.length - 1;
      while (i > 0 && argTypes[i] == Object.class) {
        i--;
      }

      Class<?> current = argTypes[i];
      if (current == Object.class) {
        if (i == 0) {
          throw new NoSuchMethodException(name + ": " + nsme.getMessage());
        } else {
          current = argTypes[i - 1];
          argTypes[i] = origTypes[i];

          if (current.isArray()) {
            argTypes[i - 1] = arraySuper(current);
          }
          return bestMethod(type, name, origTypes, argTypes);
        }
      }

      Method method = null;
      if (current.isArray()) {
        argTypes[i] = arraySuper(current);
      } else if (current.isPrimitive()) {
        method = tryBoxing(type, name, argTypes, i);
      } else if (isBoxed(current)) {
        method = tryUnboxing(type, name, argTypes, i);
      } else {
        method = tryInterfaces(type, name, argTypes, i);
      }

      if (method != null) {
        return method;
      } else {
        argTypes[i] = current.getSuperclass();
        return bestMethod(type, name, origTypes, argTypes);
      }
    }
  }

  private static Class<?> arraySuper(Class<?> current) {
    int depth = 0;
    while (current.isArray()) {
      current = current.getComponentType();
      ++depth;
    }

    if (current == Object.class) {
      return Object.class;
    } else {
      StringBuilder sb = new StringBuilder("[");
      while (--depth > 0) {
        sb.append("[");
      }

      try {
        String className = sb.append(current.getSuperclass().getName()).toString();
        return Class.forName(className);
      } catch (ClassNotFoundException cnfe) {
        throw new RuntimeException(cnfe);
      }
    }
  }

  private static Method tryBoxing(Class<?> type, String name, Class[] argTypes, int i) {
    Class<?> argType = argTypes[i];
    if (argType == boolean.class) {
      argType = Boolean.class;
    } else if (argType == char.class) {
      argType = Character.class;
    } else if (argType == byte.class) {
      argType = Byte.class;
    } else if (argType == short.class) {
      argType = Short.class;
    } else if (argType == int.class) {
      argType = Integer.class;
    } else if (argType == long.class) {
      argType = Long.class;
    } else if (argType == float.class) {
      argType = Float.class;
    } else if (argType == double.class) {
      argType = Double.class;
    }

    argTypes[i] = argType;
    try {
      return type.getMethod(name, argTypes);
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  private static Method tryUnboxing(Class<?> type, String name, Class[] argTypes, int i) {
    Class<?> argType = argTypes[i];
    if (argType == Boolean.class) {
      argType = boolean.class;
    } else if (argType == Character.class) {
      argType = char.class;
    } else if (argType == Byte.class) {
      argType = byte.class;
    } else if (argType == Short.class) {
      argType = short.class;
    } else if (argType == Integer.class) {
      argType = int.class;
    } else if (argType == Long.class) {
      argType = long.class;
    } else if (argType == Float.class) {
      argType = float.class;
    } else if (argType == Double.class) {
      argType = double.class;
    }

    argTypes[i] = argType;
    try {
      return type.getMethod(name, argTypes);
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  private static Method tryInterfaces(Class<?> type, String name, Class[] argTypes, int i) {
    Class<?> current = argTypes[i];
    for (Class<?> iface : current.getInterfaces()) {
      argTypes[i] = iface;
      try {
        return type.getMethod(name, argTypes);
      } catch (NoSuchMethodException ignored) {
      }
    }
    return null;
  }

  private static boolean isBoxed(Class<?> current) {
    return current == Boolean.TYPE
        || current == Character.TYPE
        || current == Byte.TYPE
        || current == Short.TYPE
        || current == Integer.TYPE
        || current == Long.TYPE
        || current == Float.TYPE
        || current == Double.TYPE; // This one is just an extra
  }
}
