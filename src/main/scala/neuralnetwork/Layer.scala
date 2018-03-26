package neuralnetwork

import chisel3.Bundle

class Layer extends Bundle {
  val neuralNumber = 10  //1022
  //val dataWidth = 15

 /* val io = new Bundle {
    val in0 = Flipped(Valid(UInt(dataWidth.W)))
    val in1 = Flipped(Valid(UInt(dataWidth.W)))
    val in2 = Flipped(Valid(UInt(dataWidth.W)))
    val in3 = Flipped(Valid(UInt(dataWidth.W)))
    val sel = Valid(UInt(dataWidth.W))
    val out = Valid(UInt(dataWidth.W))
  }*/
}
