package neuralnetwork
import chisel3.core.FixedPoint
import chisel3.iotesters.PeekPokeTester
import chisel3.core.fromDoubleToLiteral
import chisel3.core.fromIntToWidth
import chisel3.core.fromIntToBinaryPoint
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}

class NeuronTester(neuron: Neuron) extends PeekPokeTester(neuron) {
  poke(neuron.io.in.valid, 1)
  poke(neuron.io.in.bits, 2)

  while (peek(neuron.io.out.valid) == BigInt(0)) {
    step(1)
  }

  printf(s"[$t NeuralNetworkTester] neuralnetwork.io.out.bits = ${peek(neuron.io.out.bits)}\n")

  step(1)
}

class NeuronSpec extends FlatSpec with Matchers {
  it should "run excellently" in {
    chisel3.iotesters.Driver(() => new Neuron()) { neuron => new NeuronTester(neuron) } should be(true)
  }
}
