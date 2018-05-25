import matplotlib
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import seaborn as sns
from keras.models import load_model

epoch = 1
batch_size = 512

def load_data(filename):
	train = pd.read_csv(filename)
	return train

def preprocess(train):
	Y_train = train["label"]
	# Drop 'label' column
	X_train = train.drop(labels = ["label"],axis = 1)
	# free some space
	del train
	Y_train.value_counts()
	# Check the data
	X_train.isnull().any().describe()

	test.isnull().any().describe()

	# Normalize the data
	X_train = X_train / 255.0
	test = test / 255.0

	# Reshape image in 3 dimensions (height = 28px, width = 28px , canal = 1)
	X_train = X_train.values.reshape(-1,28,28,1)
	test = test.values.reshape(-1,28,28,1)

	# Encode labels to one hot vectors (ex : 2 -> [0,0,1,0,0,0,0,0,0,0])
	Y_train = to_categorical(Y_train, num_classes = 10)

	# Set the random seed
	random_seed = 2

	# Split the train and the validation set for the fitting
	X_train, X_val, Y_train, Y_val = train_test_split(X_train, Y_train, test_size = 0.1, random_state=random_seed)
	return [X_train, X_val, Y_train, Y_val]

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

def save_model(model,pathname):
	# save
	# print('test before save: ', model.predict(X_test[0:2]))
	# model.save('my_model.h5')   # HDF5 file, you have to pip3 install h5py if don't have it
	# More details in: https://keras-cn.readthedocs.io/en/latest/models/about_model/
	model.save('my_model.h5')
	# model = load_model('my_model.h5')
	# print('test after load: ', model.predict(X_test[0:2]))

if __name__ == '__main__':
	train = load_data("./train.csv")
	X_train, X_val, Y_train, Y_val = preprocess(train)
	model = build_model()
	save_model(model,"my_model.h5")
