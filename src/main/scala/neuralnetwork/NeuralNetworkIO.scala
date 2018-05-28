package neuralnetwork

import chisel3._
import chisel3.util._

class NeuralNetworkIO extends Bundle{
  val weight = Vec(2, Flipped(Decoupled(new Weight)))
  val in = Flipped(Decoupled(new LayerData))
  val out = Decoupled(new LayerData)
}
