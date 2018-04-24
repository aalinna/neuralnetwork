package neuralnetwork
import chisel3.core.FixedPoint
import chisel3.iotesters.PeekPokeTester
import chisel3.core.fromDoubleToLiteral
import chisel3.core.fromIntToWidth
import chisel3.core.fromIntToBinaryPoint
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}

class NeuronTester(neuron: Neuron) extends PeekPokeTester(neuron) {
  val weight = 2
  val in = 2

  (0 until neuron.numAxons).foreach { axon =>
    poke(neuron.io.in.valid, true)

    poke(neuron.io.in.bits.axon, axon)
    poke(neuron.io.in.bits.weight, weight)
    poke(neuron.io.in.bits.in, in)

    step(1)
  }

  poke(neuron.io.in.valid, false)

  poke(neuron.io.out.ready, true)

  while (peek(neuron.io.out.valid) == BigInt(0)) {
    step(1)
  }

  expect(neuron.io.out.bits.out, weight * in * neuron.numAxons)

  step(1)
}

object NeuronTester extends App {
    chisel3.iotesters.Driver(() => new Neuron(7)) { neuron => new NeuronTester(neuron) }
}
