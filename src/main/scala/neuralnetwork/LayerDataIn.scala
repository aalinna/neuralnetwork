package neuralnetwork

import chisel3._
import chisel3.util._

class LayerDataIn extends Bundle { //定义层数据输入
  val axon = UInt(32.W)
  val in = UInt(32.W)
}
