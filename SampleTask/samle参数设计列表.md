对应代码的`build_model()`部分：

```python
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

    model.compile(loss="mse", optimizer="rmsprop", metrics=['mae', 'mape'])
    
    return model
```

### 参数列表设计

1. 全局add，可选参数为{model,optimizer,epochs,batch_size}

2. 生成了model之后，model一行的可选参数为{Sequential(),Model()}

3. 生成了layers之后，可以在里面填入4个参数，分别对应每层的输入tensor size.

4. 生成的optimizer的参数有一个，为一个String.

5. epochs和batch_size同上，int.

6. 生成了上述三个之后，开始给网络增加层数。

7. 选中model后自动增加一行`model.add()` or `model.compile()`，其中的可选参数为

   - RNN

   - simpleRNN

   - LSTM

   - convLSTM2D

   - GRU

     > 示例代码里我们使用了GRU，这里贴出GRU的默认参数，有点多，我们先考虑使用到`bias_initializer`之前的。

     ```python
     keras.layers.GRU(units, activation='tanh', recurrent_activation='hard_sigmoid', use_bias=True, kernel_initializer='glorot_uniform', recurrent_initializer='orthogonal', bias_initializer='zeros', kernel_regularizer=None, recurrent_regularizer=None, bias_regularizer=None, activity_regularizer=None, kernel_constraint=None, recurrent_constraint=None, bias_constraint=None, dropout=0.0, recurrent_dropout=0.0, implementation=1, return_sequences=False, return_state=False, go_backwards=False, stateful=False, unroll=False, reset_after=False)
     ```

   - Dense

   - Activation

   - Dropout

   - Flatten

   - Input

   - Reshape

   - Permute

   - RepeatVector

   - Lambda

8. 选完参数之后，选中model，点击`model.compile()`，结束此次设计。

> Keras有两种类型的模型，[序贯模型（Sequential](http://keras-cn.readthedocs.io/en/latest/models/sequential/)）和[函数式模型（Model）](http://keras-cn.readthedocs.io/en/latest/models/model/)



