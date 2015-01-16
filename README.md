[![Build Status](https://travis-ci.org/G-Node/gndata-editor.png?branch=master)](https://travis-ci.org/G-Node/gndata-editor)
[![Coverage Status](https://coveralls.io/repos/G-Node/gndata-editor/badge.png?branch=master)](https://coveralls.io/r/G-Node/gndata-editor?branch=master)

About the gndata-editor
=======================

An application for data- and metadata management in the domain of neuroscience. This application is currently in the pre-alpha stage of development.


Getting Started
===============

**Build gndata-editor under Ubuntu 14.04**

_Dependencies_

To build the gndata-editor the following applications have to be available:

- Java version 1.8.x or higher
- Maven version 3.x.x or higher

_Build instructions_

First download [this sample project](https://github.com/G-Node/gndata-editor/wiki/sample.zip) and extract it to a directory of your choice. Next follow these build steps:

```
# git clone the current version of the gndata-editor into directory of your choice
git clone https://github.com/G-Node/gndata-editor.git

# move into the "gndata-editor" directory containing the "pom.xml" file
# and build the project using maven
mvn clean compile package install

# open the built application and use the provided sample as reference project
java -jar target/gndata-*.jar
```
