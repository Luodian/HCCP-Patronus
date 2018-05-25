import matplotlib.pyplot as plt
import numpy as np
import time
import csv
from keras.layers.core import Dense, Activation, Dropout
from keras.layers.recurrent import LSTM, GRU
from keras.models import Sequential, load_model
import h5py

np.random.seed(2017)

def data_bike_num(path_to_dataset='./bike_rnn.csv',
                           sequence_length=20,
                           ratio=1.0):

    max_values = ratio * 45949

    with open(path_to_dataset) as f:
        data = csv.reader(f, delimiter=",")
        next(data, None)  # skip the headers
        # print(len(data))
        bikes = []
        nb_of_values = 0
        for line in data:
            try:
                bikes.append(float(line[0]))
                nb_of_values += 1
            except ValueError:
                pass
            if nb_of_values >= max_values:
                break

    print ("Data loaded from csv. Formatting...")
    print(len(bikes))
    result = []
    for index in range(len(bikes) - sequence_length):
        result.append(bikes[index: index + sequence_length])
    result = np.array(result)  # shape (2049230, 50)

    result_mean = result.mean()
    result -= result_mean
    print("Shift: ", result_mean)
    print ("Data: ", result.shape)

    row = int(round(0.95 * result.shape[0]))
    train = result[:row, :]
    np.random.shuffle(train)
    X_train = train[:, :-1]
    y_train = train[:, -1]
    X_test = result[row:, :-1] # 2297
    y_test = result[row:, -1]

    X_train = np.reshape(X_train, (X_train.shape[0], X_train.shape[1], 1))
    X_test = np.reshape(X_test, (X_test.shape[0], X_test.shape[1], 1))

    return [X_train, y_train, X_test, y_test, result_mean]


def build_model():
    model = Sequential()
    layers = [1, 50, 100, 1]

    model.add(GRU(
        layers[1],
        input_shape=(None, layers[0]),
        return_sequences=True))
    model.add(Dropout(0.2))

    model.add(GRU(
        layers[2],
        return_sequences=False))
    model.add(Dropout(0.2))

    model.add(Dense(
        layers[3]))
    model.add(Activation("linear"))

    start = time.time()
    model.compile(loss="mse", optimizer="rmsprop", metrics=['mae', 'mape'])
    print ("Compilation Time : ", time.time() - start)
    return model


def run_network(model=None, data=None):
    global_start_time = time.time()
    epochs = 2
    ratio = 1
    sequence_length = 20
    path_to_dataset = './bike_rnn.csv'

    if data is None:
        print ('Loading data... ')
        X_train, y_train, X_test, y_test, result_mean = data_bike_num(
            path_to_dataset, sequence_length, ratio)
    else:
        X_train, y_train, X_test, y_test = data

    print ('\nData Loaded. Compiling...\n')

    model = load_model('bike.h5')
    # model.fit(
    #     X_train, y_train,
    #     batch_size=512, epochs=epochs, validation_split=0.05)
    # model.save('bike.h5')
    predicted = model.predict(X_test)
    predicted = np.reshape(predicted, (predicted.size,))

    try:
        # Evaluate
        scores = model.evaluate(X_test, y_test, batch_size=512)
        print("\nevaluate result: \nmse={:.6f}\nmae={:.6f}\nmape={:.6f}".format(scores[0], scores[1], scores[2]))

        # draw the figure
        y_test += result_mean
        predicted += result_mean

        fig = plt.figure()
        ax = fig.add_subplot(111)
        ax.plot(y_test,label="Real")
        ax.legend(loc='upper left')
        plt.plot(predicted,label="Prediction")
        plt.legend(loc='upper left')
        plt.show()

    except Exception as e:
        print (str(e))
    print ('Training duration (s) : ', time.time() - global_start_time)

    return model, y_test, predicted

if __name__ == '__main__':
    # data_bike_num()
    run_network()
