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

package ist.meic.pa.GenericFunctions.function;

import ist.meic.pa.GenericFunctions.GenericFunction;
import java.util.Vector;

@GenericFunction
public interface Com {

  // Test B and D
  public static Object bine(Object a) {
    return "Object";
  }

  public static String bine(String a) {
    return a;
  }

  public static Integer bine(Integer a) {
    return a;
  }

  public static Object bine(Object[] arr) {
    StringBuilder res = new StringBuilder();
    for (Object o : arr) {
      res.append(bine(o));
    }
    return res.toString();
  }

  public static Object bine(Integer[] arr) {
    int res = 0;
    for (int o : arr) {
      res += bine(o);
    }
    return res;
  }

  // Test 17
  public static Object bine(Object a, Object b) {
    Vector<Object> v = new Vector<>();
    v.add(a);
    v.add(b);
    return v;
  }

  public static Integer bine(Integer a, Integer b) {
    return a + b;
  }

  public static Object bine(String a, Object b) {
    return a + ", " + b + "!";
  }

  public static Object bine(String a, Integer b) {
    return (b == 0) ? "" : a + bine(a, b - 1);
  }
}
