function train_and_test(train_data, num_inputs, num_hidden, num_outputs, learning_rate, num_epochs)
    nn = NeuralNetwork(num_inputs, num_hidden, num_outputs, learning_rate);

    disp(nn);

    disp('Training..')
    
    for epoch = 1:num_epochs
        for i = 1:size(train_data, 1)
            inputs = train_data(i, 2:end) / 255.0 * 0.99 + 0.01;

            targets = zeros(1, num_outputs) + 0.01;
            targets(1, train_data(i, 1) + 1) = 0.99;

            train(nn, inputs, targets);
        end
    end
    
    save_weights(nn);
end

