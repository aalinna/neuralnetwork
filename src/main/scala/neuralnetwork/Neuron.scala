package neuralnetwork

import chisel3._
import chisel3.util._

class NeuronIO extends Bundle {
  val in = Flipped(Valid(UInt(Config.dataWidth.W)))
  val weight = Flipped(Valid(UInt(Config.dataWidth.W)))

  val out = Valid(UInt(Config.dataWidth.W))
}

class Neuron extends Module with CurrentCycle {
  val io = IO(new NeuronIO)

  io.out.valid := false.B
  io.out.bits := DontCare

  when(io.in.valid) {
    io.out.valid := true.B
    io.out.bits := 3.U
  }

  when(io.in.valid) {
    printf(p"[$currentCycle NeuralNetwork] io.in.bits = ${io.in.bits}\n")
  }

  when(io.out.valid) {
    printf(p"[$currentCycle NeuralNetwork] io.out.bits = ${io.out.bits}\n")
  }
}

object Neuron extends App {
  Driver.execute(Array("-td", "source/"), () => new Neuron())
}
