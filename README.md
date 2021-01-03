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
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.gservlet/gservlet-api/badge.png)](https://maven-badges.herokuapp.com/maven-central/org.gservlet/gservlet-api/)
[![Javadocs](https://javadoc.io/badge/org.gservlet/gservlet-api.png)](https://javadoc.io/doc/org.gservlet/gservlet-api)
[![follow on Twitter](https://img.shields.io/twitter/follow/gservlet?style=social)](https://twitter.com/intent/follow?screen_name=gservlet)

## Table of contents
1. [Description](#description)
1. [Main Features](#main-features)
1. [Requirements](#requirements)
1. [Getting Started](#getting-started)
1. [Building from source](#building-from-source)
1. [Versioning](#versioning)
1. [Status](#status)
1. [Contributing](#contributing)
1. [License](#license)

## Description

GServlet is an open source project inspired from the [Groovlets](http://docs.groovy-lang.org/latest/html/documentation/servlet-userguide.html), which aims to use the Groovy language and its provided modules to simplify Servlet API web development.
Groovlets are Groovy scripts executed by a servlet. They are run on request, having the whole web context (request, response, etc.) bound to the evaluation context. They are much more suitable for smaller web applications. 
Compared to Java Servlets, coding in Groovy can be much simpler. It has a couple of implicit variables we can use, for example, _request_, _response_ to access the [HttpServletRequest](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpServletRequest.html), and [HttpServletResponse](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpServletResponse.html) objects. We have access to the [HttpSession](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpSession.html) with the _session_ variable. If we want to output data, we can use _out_, _sout_, and _html_. This is more like a script as it does not have a class wrapper.

### Groovlet 

```java
if (!session) {
    session = request.getSession(true)
}
if (!session.counter) {
    session.counter = 1
}
html.html {
  head {
      title('Groovy Servlet')
  }
  body {
    p("Hello, ${request.remoteHost}: ${session.counter}! ${new Date()}")
  }
}
session.counter = session.counter + 1
```

The same philosophy has been followed in the design of this API while maintaining a class-based programming approach.

### SessionCounterServlet.groovy
 
```java
import org.gservlet.annotation.Servlet

@Servlet("/counter")
class SessionCounterServlet {

  void get() {
    if (!session.counter) {
      session.counter = 1
    }
    html.html {
      head {
        title('Groovy Servlet')
      }
      body {
        p("Hello, ${request.remoteHost}: ${session.counter}! ${new Date()}")
      }
    }
    session.counter = session.counter + 1
  }

}
```

More information can be found on the project [homepage](https://gservlet.org) where you can find the online [documentation](https://gservlet.org/documentation) and the [Javadocs](https://gservlet.org/javadocs/1.0.0) for a particular release can be browsed as well.

## Main Features

* Servlet 3.1+ Support
* Groovy Scripting and Hot Reloading
* JSON, XML, HTML and JDBC Support

## Requirements

* Java 8+
* Java IDE (Eclipse, IntelliJ IDEA, NetBeans..)
* Java EE 7+ compliant WebServer (Tomcat, Wildfly, Glassfish, Payara..)

## Getting Started

If you are just getting started with GServlet, you may want to begin by creating your first project. This section shows you how to get up and running quickly. It is highly recommended to consume the GServlet API through a dependency management tool and the artifact can be found in Maven's central repository. It is named **gservlet-api** and you just need to name a dependency on it in your project.

### Maven

```xml
<dependency>
 <groupId>org.gservlet<groupId/>
 <artifactId>gservlet-api</artifactId>
 <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
 repositories {
    mavenCentral()
}

dependencies {
    compile("org.gservlet:gservlet-api:1.0.0")
}
```

### Your First Groovy Servlet

Once your Java web server is installed and configured, you can put it to work. Five steps take you from writing your first Groovy servlet to running it. These steps are as follows:

1. Create a dynamic web project
2. Create the *_groovy_* folder inside your webapp directory
3. Write the servlet source code
4. Run your Java web server
5. Call your servlet from a web browser


You can find below some examples that you can try out and for a hot reloading of your source code, set the **GSERVLET_RELOAD** environment variable to true in your IDE.

##### ProjectServlet.groovy

```java
import org.gservlet.annotation.Servlet

@Servlet("/projects")
class ProjectServlet {

	def projects = []

	void init() {
	   projects << [id : 1, name : "Groovy", url : "https://groovy-lang.org"]
	   projects << [id : 2, name : "Spring", url : "https://spring.io"]
	   projects << [id : 3, name : "Maven", url : "https://maven.apache.org"]
	}

	void get() {
	   json(projects)
	}

	void post() {
	   def project = request.body
	   projects << project
	   json(project)
	}

	void put() {
	   def project = request.body
	   int index = projects.findIndexOf { it.id == project.id }
	   projects[index] = project
	   json(project)
	}

	void delete() {
	  def project = request.body
	  int index = projects.findIndexOf { it.id == project.id }
	  json(projects.remove(index))
   }
	
}
```

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

For a deep insight of how to write your Groovy artifacts, please refer to the [developer guide](https://gservlet.org/docs/1.0.0).

## Code Examples

We have created several [code examples](https://github.com/GServlet/gservlet-examples) to help beginners to learn and gain expertise at GServlet. Checkout the appropriate branch for the version that you are using.

## Building from source

    > git clone git@github.com:gservlet/gservlet-api.git
    > cd gservlet-api
    > mvn clean install

### Documentation

The developer guide generated with Maven is based on [Asciidoctor](http://asciidoctor.org/). Only the HTML output is enabled.

    > mvn clean generate-resources -Pdocumentation

The built documentation can then be found in the following location:
  
    > ./target/generated-docs
    
On the other hand, the Javadocs can be found in the folder:

    > ./target/site/apidocs    

We use [UMLGraph](https://www.spinellis.gr/umlgraph/index.html) to generate UML class diagrams which are embedded in the Javadocs, therefore you must have [Graphviz](https://www.graphviz.org/) installed in your computer and the **GraphvizX.XX\bin** directory added to your system PATH.

## Versioning

We version GServlet by following the [Semantic Versioning](https://semver.org), which is a general template that everyone uses and understands.

## Status

The current version of GServlet is 1.0.0.

## Contributing

New contributors are always welcome. We collected some helpful hints on how to get started on our [Contribute](https://gservlet.org/contribute) page.

## License

GServlet is an Open Source software released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
