package neuralnetwork


import chisel3._
import chisel3.util._

class NeuronDataOut extends Bundle { //数据输出
  val out = UInt(32.W)
}
