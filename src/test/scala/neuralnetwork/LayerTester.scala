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

  (0 until layer.numNeurons).foreach { neuron =>
    (0 until layer.numAxons).foreach { axon =>
      poke(layer.io.weight.valid, false)
    }
  }

  (0 until layer.numAxons).foreach { axon =>
    poke(layer.io.in.valid, true)
    poke(layer.io.in.bits.index, axon)
    poke(layer.io.in.bits.data, 2)

    step(1)
  }

  poke(layer.io.in.valid, false)

  (0 until layer.numNeurons).foreach { neuron =>
    poke(layer.io.out.ready, true)
  }

  (0 until layer.numNeurons).foreach { neuron =>
    while (peek(layer.io.out.valid) == BigInt(0)) {
      step(1)
    }

    val neuron = peek(layer.io.out.bits.index)
    val out = peek(layer.io.out.bits.data)

    printf(s"[$t LayerTester] neuron: $neuron, out: $out\n")

    step(1)
  }

    (0 until layer.numNeurons).foreach { neuron =>
      poke(layer.io.out.ready,false)
  }

  step(1)
}

object LayerTester extends App {
  chisel3.iotesters.Driver(() => new Layer(0, 5, 7)) { layer => new LayerTester(layer) }
}
