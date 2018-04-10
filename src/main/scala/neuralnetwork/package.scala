import chisel3.core.FixedPoint

import chisel3._
import chisel3.util._

package object neuralnetwork {
  def Fixed = FixedPoint(Config.fixedPointWidth.W, Config.fixedPointBinaryPoint.BP)

  implicit class fromDoubleToNeuralLiteral(val d: Double) {
    def Fixed = d.F(Config.fixedPointWidth.W, Config.fixedPointBinaryPoint.BP)
  }
}