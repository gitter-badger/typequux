/**
  * Copyright 2016 Harshad Deo
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package typequux.constraint

import typequux._
import typequux._

trait SplitAtRightConstraint[N, HL, L, R] {
  def apply(hl: HL): (L, R)
}

object SplitAtRightConstraint {

  implicit def hlSplitAtRightConstraint[N, HL <: HList, Before <: HList, At, After <: HList, L <: Dense, D](
      implicit ev0: LengthConstraint[HL, L],
      ev1: DenseDiff[L, N, D],
      ev2: PIndexer[D, HL, Before, At, After]): SplitAtRightConstraint[N, HL, Before, At :+: After] =
    new SplitAtRightConstraint[N, HL, Before, At :+: After] {
      override def apply(hl: HL) = {
        val (before, at, after) = ev2(hl)
        (before, at :+: after)
      }
    }

  implicit def tpSplitAtRightConstraint[N, T, L, R, HL <: HList, HLL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: SplitAtRightConstraint[N, HL, HLL, HLR],
      ev2: HList2TupleConverter[L, HLL],
      ev3: HList2TupleConverter[R, HLR]): SplitAtRightConstraint[N, T, L, R] = new SplitAtRightConstraint[N, T, L, R] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val (l, r) = ev1(hl)
      (ev2(l), ev3(r))
    }
  }
}
