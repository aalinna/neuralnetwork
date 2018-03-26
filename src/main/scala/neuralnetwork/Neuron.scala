package neuralnetwork

import chisel3._
import chisel3.core.FixedPoint
import chisel3.util._

class NeuronIO extends Bundle {
  val in = Flipped(Decoupled(Fixed))
  val weight = Flipped(Decoupled(Fixed))

  val out = Decoupled(Fixed)
}

class Neuron extends Module with CurrentCycle {
  val io = IO(new NeuronIO)

  io.in.ready := false.B
  io.weight.ready := false.B

  val sum = RegInit(0.Fixed)

  when(io.in.valid){
    sum := sum + io.weight.bits * io.in.bits
  }

  io.out := sum

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
