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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;

class GenericFunctionTranslator implements Translator {

  @Override
  public void start(ClassPool pool) {
    // unused
  }

  @Override
  public void onLoad(ClassPool pool, String className)
      throws NotFoundException, CannotCompileException {
    CtClass target = pool.get(className);
    if (target.hasAnnotation(GenericFunction.class)) {
      // long start = System.currentTimeMillis();
      makeGeneric(target);
      // System.out.println(System.currentTimeMillis() - start);
      // try {
      //   target.writeFile();
      // } catch (IOException ignored) {
      // }
    }
  }

  private void makeGeneric(CtClass function) throws NotFoundException, CannotCompileException {
    ArrayList<CtMethod> methods = Arrays.stream(function.getDeclaredMethods())
        .sorted((o1, o2) -> canDowncastTo(o1, o2) ? -1 : 1) // Least specific first
        .collect(Collectors.toCollection(ArrayList::new));

    while (methods.size() > 0) {
      CtMethod leastSpecific = methods.remove(0);

      for (CtMethod target : methods) {
        if (!(target.hasAnnotation(BeforeMethod.class)
            || target.hasAnnotation(AfterMethod.class))) {
          if (canDowncastTo(leastSpecific, target)) {
            instrument(leastSpecific, target, methods);
          }
        }
      }
    }
  }

  private boolean canDowncastTo(CtMethod source, CtMethod dest) {
    try {
      CtClass[] srcParams = source.getParameterTypes();
      CtClass[] dstParams = dest.getParameterTypes();
      if (srcParams.length != dstParams.length) {
        return false;
      }

      for (int i = 0; i < srcParams.length; i++) {
        CtClass src = srcParams[i];
        CtClass dst = dstParams[i];
        if (src.isArray() && dst.isArray()) {
          src = src.getComponentType();
          dst = dst.getComponentType();
        }

        if (!src.equals(dst) && !src.getName().equals(Object.class.getName())) {
          return dst.subclassOf(src);
        }
      }
    } catch (NotFoundException nfe) {
      throw new RuntimeException(nfe);
    }

    return true;
  }

  private void instrument(CtMethod caller, CtMethod target, List<CtMethod> methods)
      throws NotFoundException, CannotCompileException {
    StringBuilder template = new StringBuilder("if (");
    StringBuilder call = new StringBuilder(target.getName() + "(");

    CtClass[] params = target.getParameterTypes();
    for (int i = 0; i < params.length; i++) {
      String arg = params[i].getName();

      if (i > 0) {
        template.append(" && ");
        call.append(", ");
      }
      template.append("$").append(i + 1).append(" instanceof ").append(arg);
      call.append("(").append(arg).append(")").append("$").append(i + 1);
    }
    template.append(") {\n  ");
    call.append(")");

    if (caller.getReturnType().equals(CtClass.voidType)) {
      template.append(call.toString()).append("; return");
    } else {
      template.append("return ");
      if (!target.getReturnType().equals(CtClass.voidType)) {
        template.append(call.toString());
      } else {
        template.append("null");
      }
    }
    template.append(";\n}");

    caller.insertBefore(template.toString());
  }
}
