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

import cats.data.Xor

/**
  * Contains convenience functions to load DL4J networks using a common naming
  * schemes.
  */
object NetworkLoader {
  import scala.io.Source
  import org.deeplearning4j.nn.conf.MultiLayerConfiguration
  import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
  import org.nd4j.linalg.api.ndarray.INDArray
  import org.nd4j.linalg.factory.Nd4j
  import java.io._

  /**
    * Constructs the ``MultiLayerNetwork`` from two files
    * at the given ``basePath``. The three files are
    *
    * - the network configuration in ``basePath.json``
    * - the network parameters in ``basePath.bin``
    *
    * @param basePath the base path
    * @return error or loaded & initialized ``MultiLayerNetwork``
    */
  def loadMultiLayerNetwork(basePath: String): Throwable Xor MultiLayerNetwork = {

    def loadNetworkConfiguration(configFile: String): Throwable Xor MultiLayerConfiguration = Xor.catchNonFatal {
      if (!new File(configFile).exists()) Xor.left(new FileNotFoundException(configFile))
      val configJson = Source.fromFile(configFile).mkString
      MultiLayerConfiguration.fromJson(configJson)
    }

    def loadParams(paramsFile: String): Throwable Xor INDArray = Xor.catchNonFatal {
      if (!new File(paramsFile).exists()) Xor.left(new FileNotFoundException(paramsFile))
      val is = new DataInputStream(new BufferedInputStream(new FileInputStream(paramsFile)))
      val params = Nd4j.read(is)
      is.close()
      params
    }

    def initializeNetwork(configuration: MultiLayerConfiguration, params: INDArray): MultiLayerNetwork = {
      val network = new MultiLayerNetwork(configuration)
      network.init()
      network.setParams(params)
      network
    }

    val configFile = s"$basePath.json"
    val paramsFile = s"$basePath.bin"

    for {
      configuration ← loadNetworkConfiguration(configFile)
      params ← loadParams(paramsFile)
    } yield initializeNetwork(configuration, params)
  }

}
