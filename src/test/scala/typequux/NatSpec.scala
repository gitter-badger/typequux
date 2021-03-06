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
package typequux

import shapeless.test.illTyped

/**
  * Specifications for Peano numbers
  */
class NatSpec extends BaseSpec {

  import Nat._

  /**************** Unit Tests *****************/
  type ConstInt[X] = Int
  type Rep[N <: Nat] = N#Match[ConstInt, String, Any]

  eqv[Rep[_0], String]
  eqv[Rep[_1], Int]
  eqv[Rep[_8], Int]
  illTyped { """implicitly[Rep[_0] =:= Int]""" }
  illTyped { """implicitly[Rep[_1] =:= String]""" }
  illTyped { """implicitly[Rep[_8] =:= String]""" }

  implicitly[_0 Compare _0 =:= EQ]
  implicitly[_1 Compare _1 =:= EQ]
  implicitly[_7 Compare _7 =:= EQ]
  implicitly[_0 Compare _1 =:= LT]
  implicitly[_0 Compare _7 =:= LT]
  implicitly[_1 Compare _7 =:= LT]
  implicitly[_1 Compare _0 =:= GT]
  implicitly[_7 Compare _0 =:= GT]
  implicitly[_7 Compare _1 =:= GT]
  illTyped { """implicitly[_0 Compare _0 =:= LT]""" }
  illTyped { """implicitly[_1 Compare _1 =:= LT]""" }
  illTyped { """implicitly[_7 Compare _7 =:= LT]""" }
  illTyped { """implicitly[_0 Compare _1 =:= GT]""" }
  illTyped { """implicitly[_0 Compare _7 =:= GT]""" }
  illTyped { """implicitly[_1 Compare _7 =:= GT]""" }
  illTyped { """implicitly[_1 Compare _0 =:= LT]""" }
  illTyped { """implicitly[_7 Compare _0 =:= LT]""" }
  illTyped { """implicitly[_7 Compare _1 =:= LT]""" }
  illTyped { """implicitly[_0 Compare _0 =:= GT]""" }
  illTyped { """implicitly[_1 Compare _1 =:= GT]""" }
  illTyped { """implicitly[_7 Compare _7 =:= GT]""" }
  illTyped { """implicitly[_0 Compare _1 =:= EQ]""" }
  illTyped { """implicitly[_0 Compare _7 =:= EQ]""" }
  illTyped { """implicitly[_1 Compare _7 =:= EQ]""" }
  illTyped { """implicitly[_1 Compare _0 =:= EQ]""" }
  illTyped { """implicitly[_7 Compare _0 =:= EQ]""" }
  illTyped { """implicitly[_7 Compare _1 =:= EQ]""" }

  type C = _0#FoldR[Int, AnyVal, Fold[Nat, AnyVal]]
  implicitly[C =:= Int]
  illTyped { """implicitly[C =:= AnyVal]""" }
  type D = _0#FoldL[String, AnyRef, Fold[Nat, AnyRef]]
  implicitly[D =:= String]
  illTyped { """implicitly[D =:= AnyRef]""" }

  // basic sum tests
  isTrue[_0 + _0 === _0]
  isTrue[_0 + _1 === _1]
  isTrue[_0 + _5 === _5]
  isTrue[_1 + _0 === _1]
  isTrue[_5 + _0 === _5]
  isTrue[_2 + _5 === _7]
  isTrue[_5 + _2 === _7]

  // basic product tests
  isTrue[_0 * _0 === _0]
  isTrue[_1 * _0 === _0]
  isTrue[_0 * _1 === _0]
  isTrue[_1 * _1 === _1]
  isTrue[_1 * _9 === _9]
  isTrue[_9 * _1 === _9]
  isTrue[_2 * _2 === _4]
  isTrue[_2 * _3 === _6]
  isTrue[_3 * _2 === _6]
  isTrue[_2 * _4 === _8]
  isTrue[_4 * _2 === _8]
  isTrue[_3 * _3 === _9]

  // basic factorial tests
  isTrue[Fact[_0] === _1]
  isTrue[Fact[_1] === _1]
  isTrue[Fact[_2] === _2]
  isTrue[Fact[_3] === _6]
  isTrue[*[_6, _4] === Fact[_4]]
  isTrue[*[Fact[_4], _5] === Fact[_5]]

  // basic exponentiation tests
  isTrue[_0 ^ _1 === _0]
  isTrue[_1 ^ _0 === _1]
  isTrue[_1 ^ _1 === _1]
  isTrue[_5 ^ _0 === _1]
  isTrue[_5 ^ _1 === _5]
  isTrue[_2 ^ _2 === _4]
  isTrue[_2 ^ _3 === _8]
  isTrue[_3 ^ _2 === _9]
  isTrue[_3 ^ _3 === *[_9, _3]]

  // some more tests
  isTrue[+[_1, *[_8, Succ[_9]]] === Sq[_9]]
  isTrue[*[Sq[_4], Sq[_4]] === ^[_2, _8]]

  implicitly[NatDiff[_0, _0, _0]]
  implicitly[NatDiff[_8, _3, _5]]
  implicitly[NatDiff[_9, _0, _9]]
  implicitly[NatDiff[_6, _2, _4]]
  illTyped { """implicitly[NatDiff[_0, _1, _1]]""" }

  /************************* Property Tests *********************/
  // binary properties

  class AdditiveCommutativity[A, B]
  implicit def toAC[A <: Nat, B <: Nat](implicit ev: +[A, B] =:= +[B, A]): AdditiveCommutativity[A, B] =
    new AdditiveCommutativity[A, B]

  class MultiplicativeCommutativity[A, B]
  implicit def toMC[A <: Nat, B <: Nat](implicit ev: *[A, B] =:= *[B, A]): MultiplicativeCommutativity[A, B] =
    new MultiplicativeCommutativity[A, B]

  import Bool._

  class TotalOrderAntisymmetry[A, B]
  implicit def toTOA[A <: Nat, B <: Nat](
      implicit ev: IsTrue[&&[A <= B, B <= A] ->> ===[A, B]]): TotalOrderAntisymmetry[A, B] =
    new TotalOrderAntisymmetry[A, B]

  class TotalOrderTotality[A, B]
  implicit def toTotalOrderTotality[A <: Nat, B <: Nat](
      implicit ev: IsTrue[||[A <= B, B <= A]]): TotalOrderTotality[A, B] =
    new TotalOrderTotality[A, B]

  // cant really test these for large numbers without blowing up the compiler
  // dense numbers should be used instead
  def binaryLaws[A, B](implicit ev0: AdditiveCommutativity[A, B],
                       ev1: MultiplicativeCommutativity[A, B],
                       ev2: TotalOrderAntisymmetry[A, B],
                       ev3: TotalOrderTotality[A, B]) = true
  binaryLaws[_0, _0]
  binaryLaws[_1, _9]
  binaryLaws[_0, _3]
  binaryLaws[_1, _1]
  binaryLaws[_0, _1]
  binaryLaws[_2, _2]
  binaryLaws[_2, _4]
  binaryLaws[_2, _8]
  binaryLaws[_2, _7]
  binaryLaws[_1, _2]
  binaryLaws[_3, _3]

  // ternary properties

  class AdditiveAssociativity[X, Y, Z]
  implicit def toAA[X <: Nat, Y <: Nat, Z <: Nat](
      implicit ev: +[X, +[Y, Z]] =:= +[+[X, Y], Z]): AdditiveAssociativity[X, Y, Z] =
    new AdditiveAssociativity[X, Y, Z]

  class MultiplicativeAssociativity[X, Y, Z]
  implicit def toMA[X <: Nat, Y <: Nat, Z <: Nat](
      implicit ev: *[X, *[Y, Z]] =:= *[*[X, Y], Z]): MultiplicativeAssociativity[X, Y, Z] =
    new MultiplicativeAssociativity[X, Y, Z]

  class Distributivity[X, Y, Z]
  implicit def toDist[X <: Nat, Y <: Nat, Z <: Nat](
      implicit ev: *[+[X, Y], Z] =:= +[*[X, Z], *[Y, Z]]): Distributivity[X, Y, Z] =
    new Distributivity[X, Y, Z]

  class TotalOrderTransitivity[X, Y, Z]
  implicit def toToT[X <: Nat, Y <: Nat, Z <: Nat](
      implicit ev: IsTrue[&&[X <= Y, Y <= Z] ->> <=[X, Z]]): TotalOrderTransitivity[X, Y, Z] =
    new TotalOrderTransitivity[X, Y, Z]

  def ternaryLaws[X <: Nat, Y <: Nat, Z <: Nat](implicit ev0: AdditiveAssociativity[X, Y, Z],
                                                ev1: MultiplicativeAssociativity[X, Y, Z],
                                                ev2: Distributivity[X, Y, Z],
                                                ev3: TotalOrderTransitivity[X, Y, Z]) = true
  ternaryLaws[_0, _0, _0]
  ternaryLaws[_1, _1, _8]
  ternaryLaws[_0, _2, _9]
  ternaryLaws[_1, _1, _5]
  ternaryLaws[_0, _1, _3]
  ternaryLaws[_0, _1, _4]
  ternaryLaws[_0, _2, _7]
  ternaryLaws[_1, _2, _5]
  ternaryLaws[_1, _2, _7]
  ternaryLaws[_1, _4, _4]
  ternaryLaws[_2, _2, _4]
  ternaryLaws[_0, _3, _6]
  ternaryLaws[_1, _3, _6]
  ternaryLaws[_0, _1, _1]
  ternaryLaws[_0, _0, _5]
  ternaryLaws[_1, _1, _1]
  ternaryLaws[_0, _0, _3]
  ternaryLaws[_0, _4, _4]
  ternaryLaws[_0, _1, _7]
  ternaryLaws[_0, _3, _5]
  ternaryLaws[_0, _1, _6]

  // unary laws

  class AdditiveIdentity[A]
  implicit def toAdditiveIdentity[A <: Nat](implicit ev0: +[A, _0] =:= A, ev1: +[_0, A] =:= A): AdditiveIdentity[A] =
    new AdditiveIdentity[A]

  class MultiplicativeIdentity[A]
  implicit def toMI[A <: Nat](implicit ev0: *[A, _1] =:= A, ev1: *[_1, A] =:= A): MultiplicativeIdentity[A] =
    new MultiplicativeIdentity[A]

  class ExpZero[A]
  implicit def toExpZero[A <: Nat](implicit ev0: ^[A, _0] =:= _1): ExpZero[A] = new ExpZero[A]

  class ExpOne[A]
  implicit def toExpOne[A <: Nat](implicit ev0: ^[A, _1] =:= A): ExpOne[A] = new ExpOne[A]

  def unaryLaws[A <: Nat](
      implicit ev0: AdditiveIdentity[A],
      ev1: MultiplicativeIdentity[A],
      ev2: ExpZero[A],
      ev3: ExpOne[A]
  ) = true
  unaryLaws[_0]
  unaryLaws[_1]
  unaryLaws[_2]
  unaryLaws[_3]
  unaryLaws[_4]
  unaryLaws[_5]
  unaryLaws[_6]
  unaryLaws[_7]
  unaryLaws[_8]
  unaryLaws[_9]

  "A peano number type" should "evaluate to integers correctly" in {
    assert(toInt[_0] == 0)
    assert(toInt[_1] == 1)
    assert(toInt[_2] == 2)
    assert(toInt[_3] == 3)
    assert(toInt[_4] == 4)
    assert(toInt[_5] == 5)
    assert(toInt[_6] == 6)
    assert(toInt[_7] == 7)
    assert(toInt[_8] == 8)
    assert(toInt[_9] == 9)
  }
}
