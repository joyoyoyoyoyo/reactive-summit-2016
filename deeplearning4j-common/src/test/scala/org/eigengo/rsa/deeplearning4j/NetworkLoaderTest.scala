/*
 * The Reactive Summit Austin talk
 * Copyright (C) 2016 Jan Machacek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.eigengo.rsa.deeplearning4j

import java.io.FileNotFoundException

import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Failure

class NetworkLoaderTest extends FlatSpec with PropertyChecks with Matchers {
  import NetworkLoader._

  it should "report missing multilayer network files" in {
    val Failure(ex) = loadMultiLayerNetwork(_ ⇒ Failure(new FileNotFoundException("not there")))
    ex should be (a[FileNotFoundException])
  }

}
