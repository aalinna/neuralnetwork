package neuralnetwork

import chisel3.Bundle
import chisel3._
import chisel3.core.FixedPoint
import chisel3.util._
import chisel3.core.VecInit

class Weight extends Bundle {  //定义权重
  val axon = UInt(32.W)
  val neuron = UInt(32.W)
  val weight = Fixed
}

class LayerDataIn extends Bundle { //定义层数据输入
  val axon = UInt(32.W)
  val in = Fixed
}

class LayerDataOut extends Bundle {//定义层数据输出
  val neuron = UInt(32.W)
  val out = Fixed
}

class LayerIO extends Bundle {     //定义层输入输出
  val weight = Flipped(Decoupled(new Weight))
  val in = Flipped(Decoupled(new LayerDataIn))
  val out = Decoupled(new LayerDataOut)
}

class Layer(val numAxons: Int, val numNeurons: Int) extends Module {   //定义层
  val io = IO(new LayerIO)

  val weights = Mem(numAxons * numNeurons, Fixed)

  val neurons = VecInit(Seq.fill(numNeurons)(Module(new Neuron(numAxons)).io))

  val s_idle :: s_weightBusy :: s_weightDone :: s_accumulateBusy :: s_accumulateDone :: Nil = Enum(5)  //定义层状态
  val state = RegInit(s_idle)   //当前状态sate

  val counterNeuron = new Counter(numNeurons)  //计数器

  switch(state) {  //层状态转换
    is(s_idle) {
      when(io.weight.valid) {
        weights.write(0.U, io.weight.bits.weight)
        state := s_weightBusy
      }
    }
    is(s_weightBusy) {
      when(io.weight.valid) {
        weights.write(numAxons.U * io.weight.bits.neuron + io.weight.bits.axon, io.weight.bits.weight)
        state := s_weightBusy
      }
      when(io.weight.bits.axon === (numAxons - 1).U && io.weight.bits.neuron === (numNeurons - 1).U) {
        state := s_weightDone
      }
    }
    is(s_weightDone) {
      when(io.in.valid) {
        state := s_accumulateBusy
      }
    }
    is(s_accumulateBusy) {
      when(io.in.bits.axon === (numAxons - 1).U) {
        state := s_accumulateDone
      }
    }
    is(s_accumulateDone) {
      when(io.out.valid){
        counterNeuron.inc()
      }

      when(counterNeuron.value === (numNeurons - 1).U ){
        state := s_weightDone
      }
    }
  }

  neurons.zipWithIndex.foreach { case(neuron, i) =>
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
}
