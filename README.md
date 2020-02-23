# PBD4, A Pseudo-Boolean-Based Implementation of the D4 Compiler

[![build status](https://gitlab.univ-artois.fr/wallon-phd/softwares/pbd4/badges/master/build.svg)](https://gitlab.univ-artois.fr/wallon-phd/softwares/pbd4/commits/master)

`PBD4` is a pseudo-Boolean based implementation of the D4 compiler and model
counter written in Java that is made to be easily extensible and reusable.

You can download PBD4 with its dependencies
[here](/../builds/artifacts/master/raw/dist/pbd4.tgz?job=build).

## Editing the source

PBD4 is developed using [Java 11](https://openjdk.java.net/projects/jdk/11/)
or higher, [Gradle](https://gradle.org/) and
[Eclipse IDE](https://www.eclipse.org/).

Installing Gradle is **not** required, as the scripts `gradlew` or
`gradlew.bat` can do the job on their own.

However, you need to install at least the JDK 11 to compile the source code,
and it is recommended to install the latest version of Eclipse if you want to
edit it.

To do so, after having installed all the needed tools, you will need to clone
the project, and then to tell Gradle to generate Eclipse's configuration files
(an access to the internet is required, since dependencies may have to be
downloaded).

```bash
$ git clone https://gitlab.univ-artois.fr/wallon-phd/softwares/pbd4.git
$ cd pbd4
$ ./gradlew eclipse
```

Once this has been done, you will be able to import the `PBD4` project into
Eclipse, by choosing `File > Import... > Existing Projects into Workspace`.
Then, locate the `pbd4` repository on your computer.
Validate, and you are done: the project should now be available in Eclipse.

## Unit testing with JUnit 5

Unit tests of PBD4 are written with JUnit 5 *Jupiter*.
Type in the following command to execute these tests and to generate
code-coverage reports:

```bash
$ ./gradlew test jacocoTestReport
```

The reports for both JUnit and JaCoCo will be put into the `build` folder.
You can browse these reports by opening the following files in your browser:

+ `build/reports/tests/test/index.html` (JUnit test reports)
+ `build/reports/jacoco/test/html/index.html` (code-coverage reports)

## Static analysis with SonarQube

This project contains a file `sonar-project.properties` which allows to execute
[SonarQube](http://www.sonarqube.org/) to analyze the source code of PBD4.

After having executed the JUnit tests, type in the following command to
execute the static analysis:

```bash
$ sonar-scanner
```

To do so, you need to have `sonar-scanner` installed and available in your
`PATH`, and to have a SonarQube server running and properly configured.

## Building PBD4 from source

To build PBD4 from sources, execute the following command:

```bash
$ ./gradlew pbd4
```

This will create a directory `dist/jars` containing the jars of PBD4 and
its dependencies.
It will also produce a gzipped-tarball `dist/pbd4.tgz`, containing
all these jars, bundled with a script for executing PBD4.

## Executing PBD4

After having built PBD4, you may want to execute it from the command line.

PBD4 provides a *wrapper* bash script (available in the `exec` directory)
to make easier its execution: `pbd4.sh`, which executes PBD4 from its jar.

By default, this script considers that the jars of PBD4 are stored in their
output directory, that is `dist/jars`.

If you have moved them, or if you do not want to execute the script from
PBD4's directory, you will need to export a variable `PBD4_HOME` set with the
path of the directory containing the jars (you may put this export into your
`.bashrc` to stop worrying about it).
You may also edit the script to alter its default behavior.

Once everything is properly set, you can run PBD4.
To get its usage, type in the following command:

```bash
$ ./exec/pbd4.sh -h
```

It will display all the options recognized by PBD4, with their purpose and
allowed values.
