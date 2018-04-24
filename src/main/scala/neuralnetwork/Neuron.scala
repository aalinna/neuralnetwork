package neuralnetwork

import chisel3._
import chisel3.util._


class Neuron(val numAxons: Int, val num: Int) extends Module with CurrentCycle { //定义神经元
  val io = IO(new NeuronIO)

  val s_idle :: s_busy :: s_done :: Nil = Enum(3) //定义神经元状态：空闲、忙、已完成
  val state = RegInit(s_idle) //当前状态state

  val sum = Reg(UInt())

  switch(state) { //各状态下任务及状态转换
    is(s_idle) {
      when(io.in.fire()) {
        sum := io.in.bits.weight * io.in.bits.in //“空闲”时可接受输入并计算
        state := s_busy //接受输入后由“空闲”转到“忙”
      }
    }
    is(s_busy) {
      when(io.in.fire()) {
        sum := sum + io.in.bits.weight * io.in.bits.in //”忙“状态下持续做计算
        when(io.in.bits.axon === (numAxons - 1).U) {
          state := s_done //计算最后一个突触后状态转为“ 完成”
        }
      }
    }
    is(s_done) {
      when(io.out.fire()) {
        state := s_idle //当数据输出之后状态转为空闲
      }
    }
  }

  io.in.ready := true.B

  io.out.valid := state === s_done
  io.out.bits.out := sum

  when(io.in.fire()) {
    printf(p"[$currentCycle Neuron#$num] in: ${io.in.bits}\n")
  }

  when(io.out.fire()) {
    printf(p"[$currentCycle Neuron#$num] out: ${io.out.bits}\n")
  }
}

object Neuron extends App {
  Driver.execute(Array("-td", "source/"), () => new Neuron(7,0))
}
