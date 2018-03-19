package neuralnetwork

import chisel3._
import chisel3.util._

class NeuralNetworkIO extends Bundle {
  val dataWidth = 15

  val in = Flipped(Valid(UInt(dataWidth.W)))
  val out = Valid(UInt(dataWidth.W))
}

class NeuralNetwork extends Module with CurrentCycle {
  val io = IO(new NeuralNetworkIO)

  io.out.valid := false.B
  io.out.bits := DontCare

  when (io.in.valid) {
    io.out.valid := true.B
    io.out.bits := 3.U
  }

  when (io.in.valid) {
    printf(p"[$currentCycle NeuralNetwork] io.in.bits = ${io.in.bits}\n")
  }

  when (io.out.valid) {
    printf(p"[$currentCycle NeuralNetwork] io.out.bits = ${io.out.bits}\n")
  }
}

object NeuralNetwork extends App {
  Driver.execute(Array("-td", "source/"), () => new NeuralNetwork())
}
