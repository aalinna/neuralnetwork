package neuralnetwork


import chisel3._
import chisel3.util._

class Weight extends Bundle { //定义权重
  val axon = UInt(32.W)
  val neuron = UInt(32.W)
  val weight = UInt(32.W)
}
