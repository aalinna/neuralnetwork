package neuralnetwork


import chisel3._
import chisel3.util._

class NeuronIO extends Bundle { //输入输出
  val in = Flipped(Decoupled(new NeuronDataIn))
  val out = Decoupled(new NeuronDataOut)
}
