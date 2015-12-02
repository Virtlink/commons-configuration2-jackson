# Jackson for Commons Configuration 2
This project adds support for FasterXML's Jackson to Apache Commons
Configuration 2.

[![Build Status](https://travis-ci.org/Virtlink/commons-configuration2-jackson.svg)](https://travis-ci.org/Virtlink/commons-configuration2-jackson) [![Coverage Status](https://coveralls.io/repos/Virtlink/commons-configuration2-jackson/badge.svg?branch=master&service=github)](https://coveralls.io/github/Virtlink/commons-configuration2-jackson?branch=master)

## Installation
TBD


## Usage
Create a new configuration through one of the configuration builders,

```
Parameters params = new Parameters();
BasicConfigurationBuilder<JsonConfiguration> builder =
    new BasicConfigurationBuilder<JsonConfiguration>(JsonConfiguration.class)
        .configure(params.basic());
JsonConfiguration config = builder.getConfiguration();
```

or instantiate the configuration class directly.

```
JsonConfiguration config = new JsonConfiguration();
```


## Questions and Issues
If you have a question, ask it on [Stack Overflow](https://stackoverflow.com/questions/tagged/apache-commons-config)
under the `apache-commons-config` tag.

If you have an issue or found a bug, please search the
[list of reported issues](https://github.com/Virtlink/commons-configuration2-jackson/issues)
for a solution or workaround, or create a new issue.


## Copyright and License
Copyright © 2015 - Daniel Pelsmaeker

The code and files in this project are licensed under the
[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
You may use the files in this project in compliance with the license.



