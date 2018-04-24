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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class ClassUtils {

  private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap;
  private static final Map<Class<?>, Class<?>> primitiveWrapperMap;

  static {
    final Map<Class<?>, Class<?>> pw = new HashMap<>();
    pw.put(Boolean.TYPE, Boolean.class);
    pw.put(Character.TYPE, Character.class);
    pw.put(Byte.TYPE, Byte.class);
    pw.put(Short.TYPE, Short.class);
    pw.put(Integer.TYPE, Integer.class);
    pw.put(Long.TYPE, Long.class);
    pw.put(Float.TYPE, Float.class);
    pw.put(Double.TYPE, Double.class);

    primitiveWrapperMap = Collections.unmodifiableMap(pw);
  }

  static {
    Map<Class<?>, Class<?>> wp = new HashMap<>();
    for (Entry<Class<?>, Class<?>> entry : ClassUtils.primitiveWrapperMap.entrySet()) {
      wp.put(entry.getValue(), entry.getKey());
    }
    wrapperPrimitiveMap = Collections.unmodifiableMap(wp);
  }

  static boolean isPrimitiveWrapper(Class<?> type) {
    return wrapperPrimitiveMap.containsKey(type);
  }

  static Class<?> primitiveToWrapper(Class<?> cls) {
    return primitiveWrapperMap.getOrDefault(cls, cls);
  }

  static Class<?> wrapperToPrimitive(Class<?> cls) {
    return wrapperPrimitiveMap.getOrDefault(cls, cls);
  }
}
