package neuralnetwork

import chisel3.UInt
import chisel3._
import chisel3.core.FixedPoint
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class LayerTester(layer: Layer) extends PeekPokeTester(layer) {
  /*todo,  wiki  ，github   extends PeekPokeTester 这怎么输入输出    ， pdf
   */
  /* //测试random
   val min = UInt(32.W)
    val max = UInt(32.W)
    val a = (new Random).nextInt(99)//
    printf(p"[$a = ${a}\n")
    */
  //LayerDataIn.in.foreach(new Random())      //初始化datain

  //numAxons
}

object LayerTester extends App {
  chisel3.iotesters.Driver(() => new Layer(5, 7)) { layer => new LayerTester(layer) }
}
