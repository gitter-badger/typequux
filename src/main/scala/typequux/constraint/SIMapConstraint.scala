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

trait SIMapConstraint[S, R] {
  def apply(s: S): R
}

object SIMapConstraint {

  implicit object SINilMapConstraint extends SIMapConstraint[SINil, Map[String, Nothing]] {
    override def apply(s: SINil): Map[String, Nothing] = Map.empty
  }

  implicit def nonEmptySIMapConstraint[MP <: DenseMap, T]: SIMapConstraint[NonEmptySI[MP, T], Map[String, T]] =
    new SIMapConstraint[NonEmptySI[MP, T], Map[String, T]] {
      override def apply(s: NonEmptySI[MP, T]) = {
        (s.keys zip s.backing).toMap
      }
    }
}
