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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

public class GenericFunctionTranslator implements Translator {

  private static final String SUFFIX_PRIMARY = "$primary";
  private static final String SUFFIX_BEFORE = "$before";
  private static final String SUFFIX_AFTER = "$after";

  @Override
  public void start(ClassPool pool) {
    // unused
  }

  @Override
  public void onLoad(ClassPool pool, String className)
      throws NotFoundException, CannotCompileException {
    CtClass target = pool.get(className);
    if (target.hasAnnotation(GenericFunction.class)) {
      makeGeneric(target);
    }
  }

  private void makeGeneric(CtClass target) throws NotFoundException, CannotCompileException {
    CtMethod[] methods = target.getDeclaredMethods();
    Arrays.sort(methods, (m1, m2) -> agree(m1, m2) ? 1 : -1); // Least specific first

    List<CtMethod> befores = new LinkedList<>();
    List<CtMethod> primaries = new LinkedList<>();
    List<CtMethod> afters = new LinkedList<>();

    for (CtMethod method : methods) {
      CtMethod newMethod = CtNewMethod.copy(method, method.getDeclaringClass(), null);
      //region Distinguish and rename original methods
      StringBuilder suffix = new StringBuilder();

      for (Object annotation : method.getAvailableAnnotations()) {
        if (annotation instanceof BeforeMethod) {
          suffix.append(SUFFIX_BEFORE);
          befores.add(method);
        } else if (annotation instanceof AfterMethod) {
          suffix.append(SUFFIX_AFTER);
          afters.add(method);
        }
      }

      if (suffix.length() == 0) {
        suffix.append(SUFFIX_PRIMARY);
        primaries.add(method);
      }

      method.setName(method.getName() + suffix.toString());
      //endregion
      //region Inject 'invoke' into new method's body and add it to the class
      newMethod.setBody(invokeMethodTemplate(newMethod));
      target.addMethod(newMethod);
      //endregion
    }
    //region For every primary, insert every 'before' and 'after' methods
    for (CtMethod primary : primaries) {
      for (CtMethod before : befores) {
        primary.insertBefore(methodCallTemplate(before));
      }
      for (CtMethod after : afters) {
        primary.insertAfter(methodCallTemplate(after));
      }
    }
    //endregion
  }

  private boolean agree(CtMethod m1, CtMethod m2) {
    try {
      CtClass[] params1 = m1.getParameterTypes();
      CtClass[] params2 = m2.getParameterTypes();
      if (params1.length != params2.length) {
        return false;
      }

      for (int i = 0; i < params1.length; i++) {
        CtClass p1 = params1[i];
        CtClass p2 = params2[i];
        if (p1.isArray() && p2.isArray()) {
          p1 = p1.getComponentType();
          p2 = p2.getComponentType();
        }
        if (p1.isArray() || p2.isArray()) {
          return false;
        }

        if (!p1.equals(p2)) {
          for (CtClass iface : p1.getInterfaces()) {
            if (iface.subclassOf(p2)) {
              return true;
            }
          }
          return p1.subclassOf(p2);
        }
      }
    } catch (NotFoundException nfe) {
      throw new RuntimeException(nfe);
    }

    return true;
  }

  private String invokeMethodTemplate(CtMethod method)
      throws NotFoundException {
    StringBuilder template = new StringBuilder();
    StringBuilder call = new StringBuilder(ReflectiveMagic.class.getName() + ".invoke(");

    if (Modifier.isStatic(method.getModifiers())) {
      call.append("$class");
    } else {
      call.append("$0");
    }
    call.append(", \"").append(method.getName()).append(SUFFIX_PRIMARY).append("\", $args)");

    if (method.getReturnType() != CtClass.voidType) {
      template.append("return ($r)");
    }
    template.append(call.toString()).append(";");

    return template.toString();
  }

  private String methodCallTemplate(CtMethod method) throws NotFoundException {
    StringBuilder template = new StringBuilder("if (");
    StringBuilder call = new StringBuilder(method.getName())
        .append("(");

    CtClass[] params = method.getParameterTypes();
    //region Build if-instanceof cascade and the call's casted arguments
    for (int i = 0; i < params.length; i++) {
      if (i > 0) {
        template.append(" && ");
        call.append(", ");
      }

      String name = params[i].getName();
      template.append("$").append(i + 1).append(" instanceof ").append(name);
      call.append("(").append(name).append(")").append("$").append(i + 1);
    }
    //endregion
    call.append(")");
    template.append(") {").append(call.toString()).append(";}");

    return template.toString();
  }
}
