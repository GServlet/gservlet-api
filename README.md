# GServlet API

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)


> GServlet is an open source project inspired from the Groovlets, aiming to use the Groovy language and its provided modules to simplify Servlet API web development.

## Table of contents
* [Description](#description)
* [Getting Started](#getting-started)
* [Building from source](#building-from-source)
* [Features](#features)
* [Status](#status)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)

## Description

GServlet is an open source project aiming to use the Groovy language and its provided modules to simplify Servlet API web development.
More information can be found on the homepage where you can find 
the online documentation and the JavaDocs can be browsed.

## Getting Started

### From Maven

```xml
<dependency>
 <groupId>gservlet<groupId/>
 <artifactId>gservlet-api</artifactId>
 <version>1.0</version>
</dependency>
```

### From Gradle

```groovy
 dependencies {
    implementation 'gservlet:gservlet-api:1.0'
 }
```

## Building from source

    > git clone git@github.com:gservlet/gservlet-api.git
    > cd gservlet-api
    > mvn clean install

### Documentation

The documentation generated with Maven is based on [Asciidoctor](http://asciidoctor.org/). By default only the HTML
output is enabled. To also generate the PDF output use:

    > mvn clean install -Pdocumentation-pdf

The built documentation can then be found in the following location:
  
    > ./target/asciidoctor/docs

### Distribution

To build the distribution bundle run:

    > mvn clean install -Pdocumentation-pdf,dist


## Features

* Awesome feature 1
* Awesome feature 2
* Awesome feature 3

## Status

The current version of GServlet is 1.0

## Contributing

New contributors are always welcome. We collected some helpful hints on how to get started on our website at Contribute to GServlet

## License

GServlet is an Open Source software released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

## Contact
