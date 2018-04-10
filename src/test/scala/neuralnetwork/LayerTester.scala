package neuralnetwork

import chisel3.iotesters.PeekPokeTester

class LayerTester(layer: Layer) extends PeekPokeTester(layer) {
  poke(layer.io.in.valid, false)

  (0 until layer.numNeurons).foreach { neuron =>
    (0 until layer.numAxons).foreach { axon =>
      poke(layer.io.weight.valid, true)
      poke(layer.io.weight.bits.axon, axon)
      poke(layer.io.weight.bits.neuron, neuron)
      poke(layer.io.weight.bits.weight, 2)

      step(1)
    }
  }

  poke(layer.io.weight.valid, false)

  (0 until layer.numAxons).foreach { axon =>
    poke(layer.io.in.valid, true)
    poke(layer.io.in.bits.axon, axon)
    poke(layer.io.in.bits.in, 2)

    step(1)
  }

  poke(layer.io.in.valid, false)
  poke(layer.io.out.ready, true)

  (0 until layer.numNeurons).foreach { _ =>
    while (peek(layer.io.out.valid) == BigInt(0)) {
      step(1)
    }

    val neuron = peek(layer.io.out.bits.neuron)
    val out = peek(layer.io.out.bits.out)

    printf(s"[$t LayerTester] neuron: $neuron, out: $out\n")

    step(1)
  }

  poke(layer.io.out.ready, false)

  step(1)
}

object LayerTester extends App {
  chisel3.iotesters.Driver(() => new Layer(5, 7)) { layer => new LayerTester(layer) }
}
