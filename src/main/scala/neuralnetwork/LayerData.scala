package neuralnetwork

import chisel3._
import chisel3.util._

class LayerData extends Bundle { //定义层数据输入输出
  val index = UInt(32.W)
  val data = UInt(32.W)
}
