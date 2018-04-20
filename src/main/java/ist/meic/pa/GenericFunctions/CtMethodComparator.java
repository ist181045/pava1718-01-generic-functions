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

import java.util.Comparator;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

class CtMethodComparator implements Comparator<CtMethod> {

  @Override
  public int compare(CtMethod o1, CtMethod o2) {
    try {
      CtClass[] params1 = o1.getParameterTypes();
      CtClass[] params2 = o2.getParameterTypes();

      if (params1.length != params2.length) {
        return params2.length - params1.length;
      }

      for (int i = 0; i < params1.length; i++) {
        CtClass p1 = params1[i];
        CtClass p2 = params2[i];

        while (p1.isArray() && p2.isArray()) {
          p1 = p1.getComponentType();
          p2 = p2.getComponentType();
        }
        if (p1.isArray() || p2.isArray()) {
          return p1.isArray() ? 1 : -1;
        }

        if (p1.equals(p2)) {
          continue;
        }

        if (p1.subclassOf(p2)) {
          return -1;
        } else if (p2.subclassOf(p1)) {
          return 1;
        }
      }
    } catch (NotFoundException ignored) {
    }

    return 0;
  }
}
