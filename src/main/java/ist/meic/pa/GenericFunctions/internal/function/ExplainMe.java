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

@GenericFunction
public class ExplainMe {

  @AfterMethod
  public static void twoThings(Number o1, Number o2) {
    System.out.println("Sniff, Sniff! Why am I the last? I'm more specific than Obj-Obj!");
  }

  @AfterMethod
  public static void twoThings(Object o1, Object o2) {
    System.out.println("Muahaha! I knew I would run after the primary!");
  }

  public static void twoThings(Number o1, Integer o2) {
    System.out.println("Woho!! I'm the primary!");

  }

  @BeforeMethod
  public static void twoThings(Integer o1, Number o2) {
    System.out.println("How come Integer-Integer is more specific than me?");
  }

  @BeforeMethod
  public static void twoThings(Integer o1, Integer o2) {
    System.out.println("Let me be the first!");

  }
}
