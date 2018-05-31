package neuralnetwork

import chisel3.iotesters.PeekPokeTester

class NeuralNetworkTester(neuralNetwork: NeuralNetwork) extends PeekPokeTester(neuralNetwork) {
  val weight = 3
  val in = 3

  poke(neuralNetwork.io.in.valid, false)

  poke(neuralNetwork.io.weight(0).valid, false)
  poke(neuralNetwork.io.weight(1).valid, false)

  (0 until neuralNetwork.num_hidden).foreach { neuron =>
    (0 until neuralNetwork.num_inputs).foreach { axon =>
      poke(neuralNetwork.io.weight(0).valid, true)
      poke(neuralNetwork.io.weight(0).bits.axon, axon)
      poke(neuralNetwork.io.weight(0).bits.neuron, neuron)
      poke(neuralNetwork.io.weight(0).bits.weight, weight)

      step(1)
    }
  }

  poke(neuralNetwork.io.weight(0).valid, false)

  (0 until neuralNetwork.num_outputs).foreach { neuron =>
    (0 until neuralNetwork.num_hidden).foreach { axon =>
      poke(neuralNetwork.io.weight(1).valid, true)
      poke(neuralNetwork.io.weight(1).bits.axon, axon)
      poke(neuralNetwork.io.weight(1).bits.neuron, neuron)
      poke(neuralNetwork.io.weight(1).bits.weight, weight)

      step(1)
    }
  }

  poke(neuralNetwork.io.weight(1).valid, false)

  (0 until neuralNetwork.num_inputs).foreach { axon =>
    poke(neuralNetwork.io.in.valid, true)
    poke(neuralNetwork.io.in.bits.index, axon)
    poke(neuralNetwork.io.in.bits.data, in)

    step(1)
  }
  poke(neuralNetwork.io.in.valid, false)

  poke(neuralNetwork.io.out.ready, true)

  (0 until neuralNetwork.num_outputs).foreach { _ =>
    while (peek(neuralNetwork.io.out.valid) == BigInt(0)) {
      step(1)
    }

    val neuron = peek(neuralNetwork.io.out.bits.index)
    val out = peek(neuralNetwork.io.out.bits.data)

    printf(s"[$t NeuralNetworkTester] neuron: $neuron, out: $out\n")

    step(1)
  }

  (0 until neuralNetwork.num_outputs).foreach { neuron =>
    poke(neuralNetwork.io.out.ready,false)
  }

  step(1)


}

object NeuralNetworkTester extends App {
  chisel3.iotesters.Driver(() => new NeuralNetwork(2, 2, 2)) { neuralNetwork => new NeuralNetworkTester(neuralNetwork) }
}

