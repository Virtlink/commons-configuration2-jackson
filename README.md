[![Travis](https://img.shields.io/travis/Virtlink/commons-configuration2-jackson.svg)](https://travis-ci.org/Virtlink/commons-configuration2-jackson)
[![GitHub version](https://badge.fury.io/gh/Virtlink%2Fcommons-configuration2-jackson.svg)](https://github.com/Virtlink/commons-configuration2-jackson/releases/latest)
[![Bintray](https://img.shields.io/bintray/v/virtlink/maven/commons-configuration2-jackson.svg)](https://bintray.com/virtlink/maven/commons-configuration2-jackson)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.virtlink.commons/commons-configuration2-jackson/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.virtlink.commons/commons-configuration2-jackson)
[![GitHub license](https://img.shields.io/github/license/Virtlink/commons-configuration2-jackson.svg)](https://github.com/Virtlink/commons-configuration2-jackson/blob/master/LICENSE)

# Jackson for Commons Configuration 2
This project adds support for FasterXML's Jackson to Apache Commons
Configuration 2.


## Installation
Add the library as a dependency to your project. In Gradle, add to your `dependencies` block

```gradle
compile 'com.virtlink.commons:commons-configuration2-jackson:0.3.1'
```

In Maven, add to your `<dependencies>` tag

```maven-pom
<dependency>
    <groupId>com.virtlink.commons</groupId>
    <artifactId>commons-configuration2-jackson</artifactId>
    <version>0.3.1</version>
</dependency>
```

Otherwise download the [latest release](https://github.com/Virtlink/commons-configuration2-jackson/releases/latest)
and add the `.jar` to your project's classpath.


## Usage
Import the project's namespace.

```java
import com.virtlink.commons.configuration2.jackson.JsonConfiguration;
```

Create a new configuration through the configuration builder,

```java
Parameters params = new Parameters();
FileBasedConfigurationBuilder<JsonConfiguration> builder
    = new FileBasedConfigurationBuilder<>(JsonConfiguration.class);
JsonConfiguration config = builder.configure(params
     .fileBased()
     .setFileName("example.json")).getConfiguration();
```

or instantiate the configuration class directly.

```java
JsonConfiguration config = new JsonConfiguration();
```

You can use the configuration to read from a `Reader`

```java
try (Reader reader = new BufferedReader(new FileReader("settings.json"))) {
	config.read(reader);
}
```

or write to a `Writer`

```java
try (Writer writer = new PrintWriter("settings.json", "UTF-8")) {
	config.write(writer);
}
```


## Questions and Issues
If you have a question, ask it on [Stack Overflow](https://stackoverflow.com/questions/tagged/apache-commons-config)
under the `apache-commons-config` tag.

If you have an issue or found a bug, please search the
[list of reported issues](https://github.com/Virtlink/commons-configuration2-jackson/issues)
for a solution or workaround, or create a new issue.


## Copyright and License
Copyright © 2015-2016 - Daniel Pelsmaeker

The code and files in this project are licensed under the
[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
You may use the files in this project in compliance with the license.



