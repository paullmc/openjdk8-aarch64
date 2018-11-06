/*
 * Copyright (c) 2016, Red Hat, Inc. and/or its affiliates.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

/*
 * @test LargeObjectAlignment
 * @summary Shenandoah crashes with -XX:ObjectAlignmentInBytes=16
 * @run main/othervm -XX:+UseShenandoahGC -XX:ObjectAlignmentInBytes=16 -Xint  LargeObjectAlignment
 * @run main/othervm -XX:+UseShenandoahGC -XX:ObjectAlignmentInBytes=16 -XX:-TieredCompilation LargeObjectAlignment
 * @run main/othervm -XX:+UseShenandoahGC -XX:ObjectAlignmentInBytes=16 -XX:TieredStopAtLevel=1 LargeObjectAlignment
 * @run main/othervm -XX:+UseShenandoahGC -XX:ObjectAlignmentInBytes=16 -XX:TieredStopAtLevel=4 LargeObjectAlignment
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LargeObjectAlignment {

  static final int  SLABS_COUNT = Integer.getInteger("slabs",  10000);
  static final int  NODE_COUNT  = Integer.getInteger("nodes",  10000);
  static final long TIME_NS     = 1000L * 1000L * Integer.getInteger("timeMs", 5000);

  static Object[] objects;

  public static void main(String[] args) throws Exception {
    objects = new Object[SLABS_COUNT];

    long start = System.nanoTime();
    while (System.nanoTime() - start < TIME_NS) {
       objects[ThreadLocalRandom.current().nextInt(SLABS_COUNT)] = createSome();
    }
  }

  public static Object createSome() {
    List<Integer> result = new ArrayList<Integer>();
    for (int c = 0; c < NODE_COUNT; c++) {
      result.add(new Integer(c));
    }
    return result;
  }

}