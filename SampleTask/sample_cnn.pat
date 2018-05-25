from keras.models import load_model
import pandas as pd

def build_model():
	model = Sequential()

	model.add(Conv2D(filters = 32, kernel_size = (5,5),padding = 'Same',
	                 activation ='relu', input_shape = (28,28,1)))
	model.add(Conv2D(filters = 32, kernel_size = (5,5),padding = 'Same',
	                 activation ='relu'))
	model.add(MaxPool2D(pool_size=(2,2)))
	model.add(Dropout(0.25))

	model.add(Conv2D(filters = 64, kernel_size = (3,3),padding = 'Same',
	                 activation ='relu'))
	model.add(Conv2D(filters = 64, kernel_size = (3,3),padding = 'Same',
	                 activation ='relu'))
	model.add(MaxPool2D(pool_size=(2,2), strides=(2,2)))
	model.add(Dropout(0.25))
	
	model.add(Flatten())
	model.add(Dense(256, activation = "relu"))
	model.add(Dropout(0.5))
	model.add(Dense(10, activation = "softmax"))

	# Define the optimizer
	optimizer = RMSprop(lr=0.001, rho=0.9, epsilon=1e-08, decay=0.0)

	# Compile the model
	model.compile(optimizer = optimizer , loss = "categorical_crossentropy", metrics=["accuracy"])
	return model