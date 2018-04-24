clear; clc; close all; format compact

disp('Loading data..')

mnist_train_data = csvread('../../../data/mnist_train.csv');
 
label = mnist_train_data(2,1);
img = reshape(mnist_train_data(2,2:end), 28,28)';
image(img);