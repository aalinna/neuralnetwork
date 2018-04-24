package neuralnetwork

import chisel3._
import chisel3.util._
import chisel3.core.VecInit

class Layer(val numAxons: Int, val numNeurons: Int) extends Module with CurrentCycle { //定义层
  val io = IO(new LayerIO)

  val weights = Mem(numAxons * numNeurons, UInt(32.W))

  val neurons = VecInit(Seq.tabulate(numNeurons)(num => Module(new Neuron(numAxons, num)).io))

  val s_idle :: s_weightBusy :: s_weightDone :: s_accumulateBusy :: s_accumulateDone :: Nil = Enum(5) //定义层状态
  val state = RegInit(s_idle) //当前状态sate

  val counterNeuron = new Counter(numNeurons) //计数器

  switch(state) { //层状态转换
    is(s_idle) {
      when(io.weight.fire()) {
        weights.write(0.U, io.weight.bits.weight)
        state := s_weightBusy
      }
    }
    is(s_weightBusy) {
      when(io.weight.fire()) {
        weights.write(numAxons.U * io.weight.bits.neuron + io.weight.bits.axon, io.weight.bits.weight)
        state := s_weightBusy
      }
      when(io.weight.bits.axon === (numAxons - 1).U && io.weight.bits.neuron === (numNeurons - 1).U) {
        state := s_weightDone
      }
    }
    is(s_weightDone) {
      when(io.in.fire()) {
        state := s_accumulateBusy
      }
    }
    is(s_accumulateBusy) {
      when(io.in.bits.axon === (numAxons - 1).U) {
        state := s_accumulateDone
      }
    }
    is(s_accumulateDone) {
      when(io.out.fire()) {
        counterNeuron.inc()
      }

      when(counterNeuron.value === (numNeurons - 1).U) {
        state := s_weightDone
      }
    }
  }

  neurons.zipWithIndex.foreach { case (neuron, i) =>
    neuron.in.valid := io.in.valid
    neuron.in.bits.axon := io.in.bits.axon
    neuron.in.bits.weight := weights.read(numAxons.U * i.U + io.in.bits.axon)
    neuron.in.bits.in := io.in.bits.in

    neuron.out.ready := false.B
  }

  io.out.valid := neurons(counterNeuron.value).out.valid
  io.out.bits.neuron := counterNeuron.value
  io.out.bits.out := neurons(counterNeuron.value).out.bits.out
  neurons(counterNeuron.value).out.ready := io.out.ready

  io.weight.ready := state === s_idle || state === s_weightBusy
  io.in.ready := state =/= s_idle && state =/= s_weightBusy

  when(io.weight.fire()) {
    printf(p"[$currentCycle Layer] state: $state, weight: ${io.weight.bits}\n")
  }

  when(io.in.fire()) {
    printf(p"[$currentCycle Layer] state: $state, in: ${io.in.bits}\n")
  }

  when(io.out.fire()) {
    printf(p"[$currentCycle Layer] state: $state, out: ${io.out.bits}\n")
  }
}
