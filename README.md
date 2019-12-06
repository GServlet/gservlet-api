# GServlet API

[![Build Status @ Travis](https://api.travis-ci.com/GServlet/gservlet-api.png?branch=master)](https://travis-ci.com/GServlet/gservlet-api)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=GServlet_gservlet-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=GServlet_gservlet-api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=GServlet_gservlet-api&metric=bugs)](https://sonarcloud.io/dashboard?id=GServlet_gservlet-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=GServlet_gservlet-api&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=GServlet_gservlet-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=GServlet_gservlet-api&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=GServlet_gservlet-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=GServlet_gservlet-api&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=GServlet_gservlet-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=GServlet_gservlet-api&metric=security_rating)](https://sonarcloud.io/dashboard?id=GServlet_gservlet-api)
[![Coverage Status](https://coveralls.io/repos/github/GServlet/gservlet-api/badge.png?branch=master)](https://coveralls.io/github/GServlet/gservlet-api?branch=master)
[![License](http://img.shields.io/:license-apache-blue.png)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![follow on Twitter](https://img.shields.io/twitter/follow/gservlet?style=social)](https://twitter.com/intent/follow?screen_name=gservlet)

## Table of contents
1. [Description](#description)
1. [Features](#features)
1. [Requirements](#requirements)
1. [Getting Started](#getting-started)
1. [Building from source](#building-from-source)
1. [Versioning](#versioning)
1. [Status](#status)
1. [Contributing](#contributing)
1. [License](#license)
1. [Contact](#contact)

## Description

GServlet is an open source project inspired from the [Groovlets](http://docs.groovy-lang.org/latest/html/documentation/servlet-userguide.html), which aims to use the Groovy language and its provided modules to simplify Servlet API web development.
Groovlets are Groovy scripts executed by a servlet. They are run on request, having the whole web context (request, response, etc.) bound to the evaluation context. They are much more suitable for smaller web applications. 
Compared to Java Servlets, coding in Groovy can be much simpler. It has a couple of implicit variables we can use, for example, _request_, _response_ to access the [_HttpServletRequest_](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpServletRequest.html), and [_HttpServletResponse_](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpServletResponse.html) objects. We have access to the [_HttpSession_](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpSession.html) with the _session_ variable. 
If we want to output data, we can use _out_, _sout_, and _html_. This is more like a script as it does not have a class wrapper.

### Groovlet 

```java
if (!session) {
    session = request.getSession(true)
}
if (!session.counter) {
    session.counter = 1
}
html.html { // html is implicitly bound to new MarkupBuilder(out)
  head {
      title('Groovy Servlet')
  }
  body {
    p("Hello, ${request.remoteHost}: ${session.counter}! ${new Date()}")
  }
}
session.counter = session.counter + 1
```

More information can be found on the project [homepage](https://gservlet.org) where you can find 
the online [documentation](https://gservlet.org/documentation) and the [JavaDocs](https://gservlet.org/javadocs/1.0) for this current release can be browsed as well.

## Features

* Groovy Scripting
* Live Development
* Hot Reloading
* JSON and XML Support
* RDMS Support

## Requirements

* Java 7+
* Java IDE (Eclipse, IntelliJ IDEA, NetBeans..)
* Java EE 6+ compliant WebServer (Tomcat, Wildfly, Glassfish, Payara..)

## Getting Started

If you are just getting started with GServlet, you may want to begin by creating your first project. This section shows you how to get up and running quickly. It is highly recommended to consume the GServlet API through a dependency management tool and the artifact can be found in Maven's central repository. it is named **gservlet-api** and you just need to name a dependency on it in your project.

### From Maven

```xml
<dependency>
 <groupId>org.gservlet<groupId/>
 <artifactId>gservlet-api</artifactId>
 <version>1.0.0</version>
</dependency>
```

### From Gradle

```groovy
 dependencies {
    implementation 'org.gservlet:gservlet-api:1.0.0'
 }
```

### Your First Groovy Servlet

##### CustomerServlet.groovy

```java
import org.gservlet.annotation.Servlet

@Servlet("/customers")
class CustomerServlet {
	
  void get() {
     def customers = []
     customers << [firstName : "Kate", lastName : "Martinez"]
     customers << [firstName : "John", lastName : "Doe"]
     customers << [firstName : "Alexandra", lastName : "Floriani"]
     customers << [firstName : "Joe", lastName : "Milner"]
     json(customers)
  }
	
}
```

### Your First Groovy Filter

##### CorsFilter.groovy 

```java
import org.gservlet.annotation.Filter

@Filter("/*")
class CorsFilter {

    void filter() {
      response.addHeader("Access-Control-Allow-Origin", "*")
      response.addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST, DELETE")
      if (request.method == "OPTIONS") {
        response.status = response.SC_ACCEPTED
        return
      }
      next()
    }
    
}
```

### Your First Groovy RequestListener

##### ServletRequestListener.groovy 

```java
import org.gservlet.annotation.RequestListener

@RequestListener
class ServletRequestListener {
	
   void requestInitialized() {
     println "request initialized"
   }
	
   void requestDestroyed() {
     println "request destroyed"
   }

}
```

For more information about how to create the other listeners, please read the [documentation](https://gservlet.org/documentation).

## Building from source

    > git clone git@github.com:gservlet/gservlet-api.git
    > cd gservlet-api
    > mvn clean install

### Documentation

The documentation generated with Maven is based on [Asciidoctor](http://asciidoctor.org/). By default only the HTML output is enabled.

    > mvn clean install -Pdocumentation

The built documentation can then be found in the following location:
  
    > ./target/generated-docs
    
On the other hand, the javadocs can be found in the folder:

    > ./target/site/apidocs    

## Versioning

We version GServlet by following [Semantic Versioning](https://semver.org), which is a general template that everyone uses and understands.

## Status

The current version of GServlet is 1.0.0

## Contributing

New contributors are always welcome. We collected some helpful hints on how to get started on our [Contribute](https://gservlet.org/contribute) page.

## License

GServlet is an Open Source software released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

## Contact
