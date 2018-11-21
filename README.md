# ECar

**E**lectric **Car** service

Distributed Systems 2018-2019, 1st semester project prototype

## Getting Started

The overall system is composed of multiple services and clients.
The main service is ECar. There are also multiple car parks spread across the city.

See the project statement for a full description of the domain and the system.


### Code identification

In all the source files (including POMs), please replace __CXX__ with your Campus: A (Alameda) or T (Tagus); and your group number with two digits.

This is important for code dependency management 
i.e. making sure that your code runs using the correct components and not someone else's.


### Prerequisites

Java Developer Kit 8 is required running on Linux, Windows or Mac.
Maven 3 is also required.

To confirm that you have them installed, open a terminal and type:

```
javac -version

mvn -version
```


### Installing

To compile and install all modules:

```
mvn clean install -DskipTests
```

The tests are skipped because they require each server to be running.

After installing, start the servers (use separate consoles for each server):

UDDI naming server:

```
cd juddi-3.3.5_tomcat-7.0.82_9090
cd bin
startup
```

Park servers:

```
cd park-ws
mvn exec:java 
mvn -Dws.i=2 exec:java
mvn -Dws-i=3 exec:java
```

ECar server:

```
cd ecar-ws
mvn exec:java
```

To run the integration tests:

```
cd ecar-ws-cli
mvn verify
```

In the end, the expected output should include the test summary line as follows:

```
Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
```


<!--
## Deployment

Add additional notes about how to deploy this on a live system
-->

## Built With

* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management
* [JAX-WS](https://javaee.github.io/metro-jax-ws/) - SOAP Web Services implementation for Java

<!--
## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.
-->


## Versioning

We use [SemVer](http://semver.org/) for versioning. 

<!--
For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 
-->

## Authors

See also the list of [contributors](https://github.com/tecnico-distsys/proj19_1/contributors) who participated in this project.

<!--
## License

This project is licensed under the ? License - see the [LICENSE.md](LICENSE.md) file for details
-->


## Acknowledgments

* Members of the Distributed Systems teaching staff
