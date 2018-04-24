function accuracy = train_and_test(train_data, test_data, num_inputs, num_hidden, num_outputs, learning_rate, num_epochs)
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

    disp('Testing..')

    scores = zeros(size(test_data, 1), 1);

    for i = 1:size(test_data, 1)
        inputs = test_data(i, 2:end) / 255.0 * 0.99 + 0.01;

        targets = zeros(1, num_outputs) + 0.01;
        targets(1, test_data(i, 1) + 1) = 0.99;

        outputs = test(nn, inputs);

        [~, output] = max(outputs);
        [~, target] = max(targets);
        
        fprintf('Output: %d, target: %d, output == target: %d\n', output, target, output == target);

        scores(i, 1) = (output == target);
    end

    accuracy = mean(scores);

    fprintf('Number of hidden nodes: %d, learning rate: %.4f, number of epochs: %d, accuracy: %.4f\n', num_hidden, learning_rate, num_epochs, accuracy);
end

