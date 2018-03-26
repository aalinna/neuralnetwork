package neuralnetwork

import chisel3.Bundle
import chisel3._
import chisel3.core.FixedPoint
import chisel3.util._
import chisel3.core.VecInit

class Weight extends Bundle {
  val axon = UInt(32.W)
  val neuron = UInt(32.W)
  val weight = Fixed
}

class LayerIO extends Bundle {
  val in = Flipped(Decoupled(Fixed))
  val weight = Flipped(Decoupled(new Weight))
}

class Layer(val numAxons: Int, val numNeurons: Int) extends Module {
  val io = IO(new LayerIO)

  io.in.ready := false.B

  val neurons = VecInit(Seq.fill(numNeurons)(Module(new Neuron).io))

  neurons.foreach { neuron =>
    neuron.in.valid := false.B
    neuron.weight.valid := false.B

    neuron.in.bits := 0.Fixed
    neuron.weight.bits := 0.Fixed

    neuron.out.ready := false.B
  }

  val mem = Mem(numAxons * numNeurons, Fixed)

  val s_idle :: s_busy :: s_done :: Nil = Enum(3)
  val state = RegInit(s_idle)

  io.weight.ready := state === s_idle || state === s_busy

  switch(state) {
    is(s_idle) {
      when(io.weight.valid) {
        mem.write(0.U, io.weight.bits.weight)
        state := s_busy
      }
    }
    is(s_busy) {
      when(io.weight.valid) {
        mem.write(numAxons.U * io.weight.bits.neuron + io.weight.bits.axon, io.weight.bits.weight)
        state := s_busy
      }
      when(io.weight.bits.axon === (numAxons - 1).U && io.weight.bits.neuron === (numNeurons - 1).U) {
        state := s_done
      }
    }
    is(s_done) {

    }
  }
}
