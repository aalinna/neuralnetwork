package neuralnetwork

import chisel3._
import chisel3.util._

class NeuralNetworkIO extends Bundle {
  val dataWidth = 15

  val in = Input(UInt(dataWidth.W))
  val out = Output(UInt(dataWidth.W))
}

class NeuralNetwork extends Module with CurrentCycle {
  val io = IO(new NeuralNetworkIO)

  io.out := 1.U

  printf(p"[$currentCycle NeuralNetwork] io.in = ${io.in}\n")
}

object NeuralNetwork extends App {
  Driver.execute(Array("-td", "source/"), () => new NeuralNetwork())
}
