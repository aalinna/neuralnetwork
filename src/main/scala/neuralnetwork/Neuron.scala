package neuralnetwork

import chisel3._
import chisel3.core.FixedPoint
import chisel3.util._

class NeuronDataIn extends Bundle {  //数据输入
  val axon = UInt(32.W)
  val weight = Fixed
  val in = Fixed
}

class NeuronDataOut extends Bundle {   //数据输出
  val out = Fixed
}

class NeuronIO extends Bundle {   //输入输出
  val in = Flipped(Decoupled(new NeuronDataIn))
  val out = Decoupled(new NeuronDataOut)
}

class Neuron(val numAxons: Int) extends Module with CurrentCycle {  //定义神经元
  val io = IO(new NeuronIO)

  val s_idle :: s_busy :: s_done :: Nil = Enum(3)  //定义神经元状态：空闲、忙、已完成
  val state = RegInit(s_idle)  //当前状态sate

  val sum = RegInit(0.Fixed)

  switch(state) {   //各状态下任务及状态转换
    is(s_idle) {
      when(io.in.valid) {
        sum := io.in.bits.weight * io.in.bits.in  //“空闲”时可接受输入并计算
        state := s_busy     //接受输入后由“空闲”转到“忙”
      }
    }
    is(s_busy) {
      when(io.in.valid) {
        sum := sum + io.in.bits.weight * io.in.bits.in   //”忙“状态下持续做计算
        when(io.in.bits.axon === (numAxons- 1).U){
          state := s_done   //计算最后一个突触后状态转为“ 完成”
        }
      }
    }
    is(s_done) {
      when(io.out.valid){
        state := s_idle  //当数据输出之后状态转为空闲
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
