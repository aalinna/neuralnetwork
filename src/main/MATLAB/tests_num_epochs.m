clear; clc; close all; format compact

disp('Loading data..')

mnist_train_data = csvread('../../../data/mnist_train.csv');

%%
num_inputs = 784;
num_hidden = 100;
num_outputs = 10;

learning_rate = 0.1;

num_epochs_range = [1]';

accuracies = zeros(size(num_epochs_range, 1), 1);

for i = 1:size(num_epochs_range, 1)
    num_epochs = num_epochs_range(i);
    train_and_test(mnist_train_data, num_inputs, num_hidden, num_outputs, learning_rate, num_epochs);
end
