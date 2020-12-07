# How to add a new sensor (top down approach)
## Adding a new `PublisherSensor`
To create a sensor we need a new `PublisherSensor`by adding a method to `PublisherSensorFactory`.
As the name anticipates, a `PublisherSensor` is the combination of a pair of related publisher
and a sensor. The former is capable of receiving data elements, as retrieved from a sensor, convert
them to an appropriate ROS message and finally publish them to a node. The latter is responsible for
collecting specific data from the device. A `PublisherSensor` then simply sends the data from the
sensor to the publisher.

### Adding the Sensor
To add a new sensor, implement a `Sensor` in the __sensors__ package by extending `BaseSensor`.
You'll need to provide:
- A way to __#start__ and __#stop__ collecting data.
- A call to __#notifyListeners__ each time new data is available.

For sensors that require an ARCore frame, we strongly suggest to receive a `LiveData<Frame>` as
a constructor parameter. You can register you sensor to it to receive frames as they are obtained
by the app and retrieve whichever data you need from the frame easily.

For an ARCore based sensor, follow `OdometrySensor` structure. Else, `ImuSensor` is a good choice.

### Adding the Publisher
To add a new publisher you'll have to implement the `MessagePublisher` interface. Publishers
shall receive a `DataToRosMessageConverter` object as parameter that will be used for performing
the conversions (so as not to couple the publishing logic with the conversion logic). You'll need
to provide:
- A publish method that will convert the data as received from a sensor and use a
__RosJava Publisher__ to publish the message.

#### Adding the Converter
Finally, you will need to implement a converter. Specifically, a `DataToRosMessageConverter` that
will convert data as received from a sensor to a __ROS Message__.

#### Adding a Data Structure
In case the newly added sensor provides data in multiple data formats, it is required that a
data structure holding all of the data is implemented. Look at `ImuData` used for `ImuSensor` for
example.