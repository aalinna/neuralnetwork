package neuralnetwork

import chisel3._
import chisel3.util._
import chisel3.core.VecInit

class NeuralNetwork() extends Module with CurrentCycle {
  val io = IO(new NeuralNetworkIO)

  val layer0 = Module(new Layer(MNIST.num_inputs, MNIST.num_hidden))
  val layer1 = Module(new Layer(MNIST.num_hidden, MNIST.num_outputs))

  io.weight(0) <> layer0.io.weight
  io.weight(1) <> layer1.io.weight

  io.in <> layer0.io.in
  layer0.io.out <> layer1.io.in
  layer1.io.out <> io.out


}

object NeuralNetwork extends App {
  Driver.execute(Array("-td", "source/"), () => new NeuralNetwork())
}
