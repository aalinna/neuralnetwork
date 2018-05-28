package neuralnetwork

import scala.io.Source

object MNIST {
  private def readCsv(fileName: String): Seq[Seq[Double]] = {
    Source.fromFile(fileName).getLines().map { line =>
      line.split(",").map(_.trim.toDouble).toSeq
    }.toSeq
  }

  val weights_InputToHidden = readCsv("data/weights_InputToHidden.csv")
  val weights_HiddenToOutput = readCsv("data/weights_HiddenToOutput.csv")
  val mnist_testData = readCsv("data/mnist_test.csv")

 val num_inputs = 784
  val num_hidden = 100
  val num_outputs = 10

  val mnist_testData_targets = mnist_testData.map(line=>line.head)
  val mnist_testData_inputs = mnist_testData.map(line=>line.tail)
}
