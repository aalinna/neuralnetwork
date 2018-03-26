package neuralnetwork

import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}

class LayerTester(layer: Layer)extends PeekPokeTester(layer){

}

class LayerSpec extends FlatSpec with Matchers {
  it should "run excellently" in {
    chisel3.iotesters.Driver(() => new Layer(5,7)) { layer => new LayerTester(layer) } should be(true)
  }
}
