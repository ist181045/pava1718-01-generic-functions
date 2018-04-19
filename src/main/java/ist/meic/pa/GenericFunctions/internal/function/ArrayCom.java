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

package ist.meic.pa.GenericFunctions.internal.function;

import ist.meic.pa.GenericFunctions.GenericFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@GenericFunction
public class ArrayCom {

  public static Object bine(Object a, Object b) {
    return new Object[]{a, b};
  }

  public static Object bine(Object[] a, Object[] b) {
    Object[] r = new Object[a.length];
    for (int i = 0; i < a.length; i++) {
      r[i] = ArrayCom.bine(a[i], b[i]);
    }
    return r;
  }

  private static Object bine(String a, String b) {
    System.out.println(a + b);
    return a + b;
  }

  private static Object bine(String a, Float b) {
    return a + "-(float): " + b;
  }

  public static Integer bine(Integer a, Integer b) {
    return a + b;
  }

  // Test K
  public static Object bine(List<Object> a, List<Object> b) {
    System.out.println("List, List");
    Object[][] r = new Object[a.size()][b.size()];
    for (int i = 0; i < a.size(); i++) {
      for (int j = 0; j < b.size(); j++) {
        r[i][j] = ArrayCom.bine(a.get(i), b.get(j));
      }
    }
    return Arrays.deepToString(r);
  }

  public static Object bine(ArrayList<Object> a, ArrayList<Object> b) {
    System.out.println("ArrayList");
    Object[][] r = new Object[a.size()][b.size()];
    for (int i = 0; i < a.size(); i++) {
      for (int j = 0; j < b.size(); j++) {
        r[i][j] = ArrayCom.bine(a.get(i), b.get(j));
      }
    }
    return Arrays.deepToString(r);
  }

}
