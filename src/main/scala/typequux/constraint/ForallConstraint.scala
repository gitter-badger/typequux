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

import util.control.Breaks._

trait ForallConstraint[T, C] {
  def apply(t: T, f: C => Boolean): Boolean
}

object ForallConstraint {

  implicit def buildForallConstraint[T, C](implicit ev: ForeachConstraint[T, C]): ForallConstraint[T, C] =
    new ForallConstraint[T, C] {
      override def apply(t: T, f: C => Boolean) = {
        var res = true
        breakable {
          ev(t) { c =>
            res = res && f(c)
            if (!res) break
          }
        }
        res
      }
    }
}
