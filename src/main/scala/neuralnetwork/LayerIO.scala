package neuralnetwork


import chisel3._
import chisel3.util._

class LayerIO extends Bundle { //定义层输入输出
  val weight = Flipped(Decoupled(new Weight))
  val in = Flipped(Decoupled(new LayerDataIn))
  val out = Decoupled(new LayerDataOut)
}
