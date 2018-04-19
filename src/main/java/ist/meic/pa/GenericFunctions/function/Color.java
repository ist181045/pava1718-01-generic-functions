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
import ist.meic.pa.GenericFunctions.domain.Black;
import ist.meic.pa.GenericFunctions.domain.Blue;
import ist.meic.pa.GenericFunctions.domain.Red;
import ist.meic.pa.GenericFunctions.domain.SuperBlack;
import ist.meic.pa.GenericFunctions.domain.Yellow;

@GenericFunction
public class Color {

  // Test A
  public static String mix(Object o) {
    return "I'm just an object.";
  }

  public static String mix(Color c1) {
    return "What color am I?";
  }

  public static String mix(Red c1) {
    return "Red";
  }

  public static String mix(Object[] arr) {
    StringBuilder res = new StringBuilder();
    for (Object o : arr) {
      res.append(mix(o));
    }
    return res.toString();
  }

  // Test M
  public static String mix(Color c1, Color c2) {
    return mix(c2, c1);
  }

  public static String mix(Red c1, Red c2) {
    return "More red";
  }

  public static String mix(Blue c1, Blue c2) {
    return "More blue";
  }

  public static String mix(Yellow c1, Yellow c2) {
    return "More yellow";
  }

  public static String mix(Red c1, Blue c2) {
    return "Magenta";
  }

  public static String mix(Red c1, Yellow c2) {
    return "Orange";
  }

  public static String mix(Blue c1, Yellow c2) {
    return "Green";
  }

  // Test N
  public static String mix(Object c1, Object c2, Object c3) {
    return "Object-Object-Object";
  }

  public static String mix(Color c1, Color c2, Color c3) {
    return "Color-Color-Color";
  }

  public static String mix(Object c1, Color c2, Color c3) {
    return "Object-Color-Color";
  }

  public static String mix(Color c1, Object c2, Color c3) {
    return "Color-Object-Color";
  }

  public static String mix(Color c1, Color c2, Object c3) {
    return "Color-Color-Object";
  }

  public static String mix(Red c1, Color c2, Color c3) {
    return "Red-Color-Color";
  }

  public static String mix(Color c1, Red c2, Color c3) {
    return "Color-Red-Color";
  }

  public static String mix(Color c1, Color c2, Red c3) {
    return "Color-Color-Red";
  }

  public static String mix(Red c1, Color c2, Red c3) {
    return "Red-Color-Red";
  }

  public static String mix(Color c1, SuperBlack c2, Color c3) {
    return "Color-SuperBlack-Color";
  }

  public static String mix(Black c1, SuperBlack c2, Color c3) {
    return "Black-SuperBlack-Color";
  }

  public static String mix(SuperBlack c1, Black c2, Color c3) {
    return "SuperBlack-Black-Color";
  }

  public static String mix(SuperBlack c1, Black c2, Black c3) {
    return "SuperBlack-Black-Black";
  }

  public static String mix(SuperBlack c1, Black c2, SuperBlack c3) {
    return "SuperBlack-Black-SuperBlack";
  }
}
