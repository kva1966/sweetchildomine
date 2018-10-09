# README

`<<<WIP>>>`

## Overview

Most significant files are annotated/commented upon, perhaps, at times, a bit 
too colorfully, in the interest of interview clarity. The author is generally 
much more conservative in production code.

This iteration only has source code and relevant tests. There are no UI 
components; presentation is limited to Gradle build and test output.

There are 2 ports:

1. Java 8
2. TypeScript 3

Since the TypeScript port is very similar to the Java 8 implementation, most
design choices or notes are only in the J8 sources. Were it a production port,
it would have the necessary documentation as well.


## Prerequisites

You must have Java 8 installed. Only been tested with the Oracle JDK. OpenJDK
should work given the relatively simple project (so long as Gradle jars can
be evaluated by OpenJDK!).


## Build and Run

Unix'ey OS instructions, adapt for Windows accordingly:

~~~
git clone https://github.com/kva1966/sweetchildomine

cd sweetchildomine

# Runs both J8 and TS build and test
./gradlew test
~~~


