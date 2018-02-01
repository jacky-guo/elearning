import numpy as np
import pandas as pd
from keras.utils import np_utils
from keras.models import Sequential
from keras.layers import Dense, Activation, Convolution2D, MaxPooling2D, Flatten,Dropout
from keras.layers.normalization import BatchNormalization
from keras.optimizers import Adam

df = pd.read_csv('data/train.csv')
X_train = []
for index in range(len(df)):
    X_train.append(np.asarray(list(map(int, df.feature[index].split()))))
test = pd.read_csv('data/test.csv')
X_test = []
for index in range(len(test)):
    X_test.append(np.asarray(list(map(int, test.feature[index].split()))))
X_train = np.asarray(X_train)
X_train = X_train.reshape(-1,1,48,48)/255.
y_train = df.label
y_train = np_utils.to_categorical(y_train, num_classes=7)
X_test = np.asarray(X_test)
X_test = X_test.reshape(-1,1,48,48)/255.
X_train.shape
# Another way to build your CNN
model = Sequential()
# Conv layer 1 output shape (32, 48, 48)
model.add(Convolution2D(
    batch_input_shape=(None, 1, 48, 48),
    filters=32,
    kernel_size=5,
    strides=1,
    padding='same',     # Padding method
    data_format='channels_first',
))
BatchNormalization(axis=-1)
model.add(Activation('relu'))
# Pooling layer 1 (max pooling) output shape (64, 24, 24)
model.add(Convolution2D(
    batch_input_shape=(None, 1, 48, 48),
    filters=64,
    kernel_size=5,
    strides=1,
    padding='same',     # Padding method
    data_format='channels_first',
))
BatchNormalization(axis=-1)
model.add(Activation('relu'))
# Pooling layer 1 (max pooling) output shape (64, 24, 24)
model.add(MaxPooling2D(
    pool_size=2,
    strides=2,
    padding='same',    # Padding method
    data_format='channels_first',
))
model.add(Dropout(0.1))

# Conv layer 2 output shape (128, 24, 24)
model.add(Convolution2D(128, 5, strides=1, padding='same', data_format='channels_first'))
BatchNormalization(axis=-1)

model.add(Activation('relu'))

model.add(MaxPooling2D(
    pool_size=2,
    strides=2,
    padding='same',    # Padding method
    data_format='channels_first',
))
model.add(Dropout(0.1))
# Conv layer 3 output shape (256, 12, 12)
model.add(Convolution2D(256, 5, strides=1, padding='same', data_format='channels_first'))
BatchNormalization(axis=-1)

model.add(Activation('relu'))
model.add(Convolution2D(512, 5, strides=1, padding='same', data_format='channels_first'))
BatchNormalization(axis=-1)

model.add(Activation('relu'))
model.add(MaxPooling2D(2, 2, 'same', data_format='channels_first'))
model.add(Dropout(0.2))

# Conv layer 4 output shape (512, 6, 6)
model.add(Convolution2D(1024, 5, strides=1, padding='same', data_format='channels_first'))
BatchNormalization(axis=-1)
model.add(Activation('relu'))
# Pooling layer 4 (max pooling) output shape (512, 3, 3)
model.add(MaxPooling2D(2, 2, 'same', data_format='channels_first'))
model.add(Dropout(0.4))

# Fully connected layer 1 input shape (512 * 3 * 3) = (4608), output shape (1536)
model.add(Flatten())
model.add(Dense(2048))
BatchNormalization(axis=-1)
model.add(Activation('relu'))
model.add(Dropout(0.5))


# Fully connected layer 2 to shape (10) for 10 classes
model.add(Dense(7))
model.add(Activation('softmax'))

# Another way to define your optimizer
adam = Adam(lr=1e-4)
# We add metrics to get more results you want to see
model.compile(optimizer=adam,
              loss='categorical_crossentropy',
              metrics=['accuracy'])

print('Training ------------')
# Another way to train the model
model.fit(X_train, y_train, epochs=20, batch_size=128,)
output = model.predict(X_test,batch_size=128)
y_classes = output.argmax(axis=-1)
sample = pd.read_csv('data/sample.csv')
sample.label = y_classes
sample.to_csv("./result/predict.csv",index = False)