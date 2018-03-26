package neuralnetwork

import chisel3._
import chisel3.core.FixedPoint
import chisel3.util._

class NeuronDataIn extends Bundle {
  val axon = UInt(32.W)
  val weight = Fixed
  val in = Fixed
}

class NeuronDataOut extends Bundle {
  val out = Fixed
}

class NeuronIO extends Bundle {
  val in = Flipped(Decoupled(new NeuronDataIn))
  val out = Decoupled(new NeuronDataOut)
}

class Neuron(val numAxons: Int) extends Module with CurrentCycle {
  val io = IO(new NeuronIO)

  val s_idle :: s_busy :: s_done :: Nil = Enum(3)
  val state = RegInit(s_idle)

  val sum = RegInit(0.Fixed)

  switch(state) {
    is(s_idle) {
      when(io.in.valid) {
        sum := io.in.bits.weight * io.in.bits.in
        state := s_busy
      }
    }
    is(s_busy) {
      when(io.in.valid) {
        sum := sum + io.in.bits.weight * io.in.bits.in
        when(io.in.bits.axon === (numAxons- 1).U){
          state := s_done
        }
      }
    }
    is(s_done) {
      when(io.out.valid){
        state := s_idle
      }
    }
  }

  io.in.ready := true.B

  io.out.valid := state === s_done
  io.out.bits := sum

  when(io.in.valid) {
    //    printf(p"[$currentCycle NeuralNetwork] io.in.bits = ${io.in.bits}\n")
  }

  when(io.out.valid) {
    //    printf(p"[$currentCycle NeuralNetwork] io.out.bits = ${io.out.bits}\n")
  }
}

object Neuron extends App {
  Driver.execute(Array("-td", "source/"), () => new Neuron(10))
}
