package neuralnetwork


import chisel3._
import chisel3.util._

class NeuronDataIn extends Bundle { //数据输入
  val axon = UInt(32.W)
  val weight = UInt(32.W)
  val in = UInt(32.W)
}
