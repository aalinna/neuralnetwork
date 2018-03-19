package neuralnetwork
import chisel3.core.FixedPoint
import chisel3.iotesters.PeekPokeTester
import chisel3.core.fromDoubleToLiteral
import chisel3.core.fromIntToWidth
import chisel3.core.fromIntToBinaryPoint
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}

class NeuralNetworkTester(neuralNetwork: NeuralNetwork) extends PeekPokeTester(neuralNetwork) {
  poke(neuralNetwork.io.in.valid, 1)
  poke(neuralNetwork.io.in.bits, 2)

  while (peek(neuralNetwork.io.out.valid) == BigInt(0)) {
    step(1)
  }

  printf(s"[$t NeuralNetworkTester] neuralnetwork.io.out.bits = ${peek(neuralNetwork.io.out.bits)}\n")

  step(1)
}

class NeuralNetworkSpec extends FlatSpec with Matchers {
  it should "run excellently" in {
    chisel3.iotesters.Driver(() => new NeuralNetwork()) { neuralNetwork => new NeuralNetworkTester(neuralNetwork) } should be(true)
  }
}
