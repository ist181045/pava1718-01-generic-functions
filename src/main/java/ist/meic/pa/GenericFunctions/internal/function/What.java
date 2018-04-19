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

import ist.meic.pa.GenericFunctions.AfterMethod;
import ist.meic.pa.GenericFunctions.BeforeMethod;
import ist.meic.pa.GenericFunctions.GenericFunction;
import ist.meic.pa.GenericFunctions.internal.domain.Black;
import ist.meic.pa.GenericFunctions.internal.domain.Blue;
import ist.meic.pa.GenericFunctions.internal.domain.Red;
import ist.meic.pa.GenericFunctions.internal.domain.SuperBlack;

@GenericFunction
public class What {

  public static void is(Black i) {
    System.out.print("What is black? ");
  }

  public static void is(Red i) {
    System.out.print("What is red? ");
  }

  @BeforeMethod
  public static void is(Blue o) {
    System.out.println("Blue ");
  }

  @AfterMethod
  public static void is(Object o) {
    System.out.print(" Is it an object?");
  }

  @AfterMethod
  public static void is(Color o) {
    System.out.print(" Is it a color?");
  }

  @AfterMethod
  public static void is(SuperBlack o) {
    System.out.print(" It is all of that and much more...");
  }
}
