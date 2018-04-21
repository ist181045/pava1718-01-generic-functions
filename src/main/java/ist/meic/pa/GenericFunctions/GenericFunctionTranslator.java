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
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

public class GenericFunctionTranslator implements Translator {

  private static final String SUFFIX_PRIMARY = "primary";

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

  private void makeGeneric(CtClass target) throws NotFoundException, CannotCompileException {
    ArrayList<CtMethod> methods = Arrays.stream(target.getDeclaredMethods())
        .sorted((o1, o2) -> agree(o1, o2) ? -1 : 1) // Least specific first
        .collect(Collectors.toCollection(ArrayList::new));

    for (CtMethod method : methods) {
      injectBestMethod(method, SUFFIX_PRIMARY);
    }
  }

  private void injectBestMethod(CtMethod method, String suffix)
      throws NotFoundException, CannotCompileException {
    CtMethod newMethod = CtNewMethod.copy(method, method.getDeclaringClass(), null);
    StringBuilder template = new StringBuilder();
    StringBuilder call = new StringBuilder(ReflectiveMagic.class.getName() + ".invoke(");

    method.setName(method.getName() + "$" + suffix);
    if (Modifier.isStatic(newMethod.getModifiers())) {
      call.append("$class");
    } else {
      call.append("$0");
    }
    call.append(", \"").append(method.getName()).append("\", new Object[]{");

    CtClass[] params = newMethod.getParameterTypes();
    for (int i = 0; i < params.length; i++) {
      if (i > 0) {
        call.append(", ");
      }
      call.append("$").append(i + 1);
    }
    call.append("})");

    if (newMethod.getReturnType() != CtClass.voidType) {
      template.append("return ($r)");
    }
    template.append(call.toString()).append(";");

    // System.out.println(template.toString());
    newMethod.setBody(template.toString());
    method.getDeclaringClass().addMethod(newMethod);
  }

  private boolean agree(CtMethod source, CtMethod dest) {
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
}
