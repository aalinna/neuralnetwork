package neuralnetwork

import chisel3._
import chisel3.util._

class LayerDataOut extends Bundle {  //定义层数据输出
  val neuron = UInt(32.W)
  val out = UInt(32.W)
}
