% Neural network.
classdef NeuralNetwork < handle
    properties
        num_inputs
        num_hidden
        num_outputs
        
        learning_rate
        
        activation_function
        
        weights_inputs_hidden
        weights_hidden_outputs
    end
    
    methods
        % Initialize the neural network.��ʼ��������
        function self = NeuralNetwork(num_inputs, num_hidden, num_outputs, learning_rate)
            self.num_inputs = num_inputs;   %�����������Ŀ
            self.num_hidden = num_hidden;   %�������ز���Ŀ
            self.num_outputs = num_outputs; %�����������Ŀ
            
            self.learning_rate = learning_rate;
            
            self.activation_function = @sigmoid;
            
            self.weights_inputs_hidden = normrnd(0.0, power(self.num_hidden, -0.5), self.num_hidden, self.num_inputs);
            self.weights_hidden_outputs = normrnd(0.0, power(self.num_outputs, -0.5), self.num_outputs, self.num_hidden);
        end
        
        % Train the neural network.ѵ��������
        function train(self, inputs, targets)
            inputs = inputs';
            targets = targets';
            
            hidden_in = self.weights_inputs_hidden * inputs;   %��������������Ϊ���ز������
            hidden_out = self.activation_function(hidden_in);  %�������������
            
            outputs_in = self.weights_hidden_outputs * hidden_out;  %�������ز������Ϊ����������
            outputs_out = self.activation_function(outputs_in);     %���������������
            
            outputs_errors = targets - outputs_out;
            hidden_errors = self.weights_hidden_outputs' * outputs_errors;
            
            self.weights_hidden_outputs = self.weights_hidden_outputs + self.learning_rate .* outputs_errors .* outputs_out .* (1 - outputs_out) * hidden_out';
            self.weights_inputs_hidden = self.weights_inputs_hidden + self.learning_rate .* hidden_errors .* hidden_out .* (1 - hidden_out) * inputs';
        end
        
        % Test the neural network.����������
        function outputs_out = test(self, inputs)
            inputs = inputs';
            
            hidden_in = self.weights_inputs_hidden * inputs;
            hidden_out = self.activation_function(hidden_in);
            
            outputs_in = self.weights_hidden_outputs * hidden_out;
            outputs_out = self.activation_function(outputs_in);
            
            outputs_out = outputs_out';
        end
        
        % save weights
        function  save_weights(self)
            csvwrite('../../../data/weights_InputToHidden.csv',self.weights_inputs_hidden);
            csvwrite('../../../data/weights_HiddenToOutput.csv',self.weights_hidden_outputs);
        end
    end

end

