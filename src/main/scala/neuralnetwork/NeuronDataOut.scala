package neuralnetwork


import chisel3._
import chisel3.util._

class NeuronDataOut extends Bundle { //数据输出
  val data = UInt(32.W)


}
