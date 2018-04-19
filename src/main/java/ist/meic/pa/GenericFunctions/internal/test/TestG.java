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

package ist.meic.pa.GenericFunctions.internal.test;

import ist.meic.pa.GenericFunctions.internal.function.ArrayCom;
import java.util.Arrays;

public class TestG {

  public static void main(String[] args) {
    println(ArrayCom.bine(1, 3));
    println(ArrayCom.bine(new Object[]{1, 2, 3}, new Object[]{4, 5, 6}));
    println(ArrayCom.bine(new Object[]{new Object[]{1, 2}, 3},
        new Object[]{new Object[]{3, 4}, 5}));
  }

  public static void println(Object obj) {
    if (obj instanceof Object[]) {
      System.out.println(Arrays.deepToString((Object[]) obj));
    } else {
      System.out.println(obj);
    }
  }
}
