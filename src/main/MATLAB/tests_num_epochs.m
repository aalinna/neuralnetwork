clear; clc; close all; format compact

disp('Loading data..')

mnist_train_data = csvread('data/mnist_train.csv');
mnist_test_data = csvread('data/mnist_test.csv');

%%
num_inputs = 784;
num_hidden = 100;
num_outputs = 10;

learning_rate = 0.1;

num_epochs_range = [1, 2, 3, 5, 6, 7, 10, 15, 20]';

accuracies = zeros(size(num_epochs_range, 1), 1);

for i = 1:size(num_epochs_range, 1)
    num_epochs = num_epochs_range(i);
    accuracy = train_and_test(mnist_train_data, mnist_test_data, num_inputs, num_hidden, num_outputs, learning_rate, num_epochs);

    accuracies(i) = accuracy;
end

%%
[best_accuracy, best_accuracy_index] = max(accuracies);
best_num_epochs = num_epochs_range(best_accuracy_index);

fprintf('Best number of epochs: %.4f, best accuracy: %.4f\n', best_num_epochs, best_accuracy);

%%
plot(num_epochs_range, accuracies, '-o')
title('Number of Epochs vs. Accuracy')
xlabel('Number of Epochs')
ylabel('Accuracy')
