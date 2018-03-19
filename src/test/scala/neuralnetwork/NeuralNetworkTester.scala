package neuralnetwork
import chisel3.core.FixedPoint
import chisel3.iotesters.PeekPokeTester
import chisel3.core.fromDoubleToLiteral
import chisel3.core.fromIntToWidth
import chisel3.core.fromIntToBinaryPoint
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}

class NeuralNetworkTester(neuralNetwork: NeuralNetwork) extends PeekPokeTester(neuralNetwork) {
  poke(neuralNetwork.io.in, 1)

  printf(s"[$t NeuralNetworkTester] neuralnetwork.io.out = ${peek(neuralNetwork.io.out)}\n")

  step(1)
}

class NeuralNetworkSpec extends FlatSpec with Matchers {
  it should "run excellently" in {
    chisel3.iotesters.Driver(() => new NeuralNetwork()) { neuralNetwork => new NeuralNetworkTester(neuralNetwork) } should be(true)
  }
}
