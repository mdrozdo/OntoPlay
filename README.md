# OntoPlay

[![Build Status](https://travis-ci.org/mdrozdo/OntoPlay.svg?branch=master)](https://travis-ci.org/mdrozdo/OntoPlay)

OntoPlay is a flexible interface for ontology-based systems. OntoPlay allows creation of dynamical interfaces that allow ontology-illiterate users to select a class or an individual on the basis of the ontology of the system. Furthermore, the OntoPlay is ontology agnostic and allows for ontology modification without the need for changing the code of the user interface.

The module has been described in several articles:
* [OntoPlay - A Flexible User-Interface for Ontology-based Systems](http://ceur-ws.org/Vol-918/111110086.pdf)
* [Graphical Interface for Ontology Mapping with Application to Access Control](https://link.springer.com/chapter/10.1007/978-3-319-54472-4_5)
* [Implementing Agent-based Resource Management in Tsunami Modeling - Preliminary Considerations](http://www.ibspan.waw.pl/~paprzyck/mp/cvr/research/AiG_papers/DNIS2014.pdf)


## Getting Started

OntoPlay is a [Play Framework](https://playframework.com/) submodule. It is meant to be included in a top level Play application, in the same way as it is used in the [OntoPlay-TAN](https://github.com/mdrozdo/OntoPlay-TAN) project. 

The project uses Play version 2.5.x. 

### Prerequisites

The project requires installation of the Play Framework (see instructions [here](https://playframework.com/documentation/2.5.x/Installing)). 

The following guide assumes creating a new Play application from a seed project and requires prior installation of [sbt 0.13.13 or higher](https://www.scala-sbt.org/download.html).

### Running standalone

The module is meant to be used as a subpackage to other Play applications, but can be run standalone for testing purposes.

To run the standalone application, run the following command from the root folder:
```
sbt "run -Dconfig.resource=sample.conf"
```

Then, navigate the browser to http://localhost:9000

### Running tests

To run the automated tests, simply execute:

```
sbt test
```

### Creating new application using OntoPlay
   
The following steps use the OntoPlay application seed, built on giter8 and hosted at https://github.com/mdrozdo/ontoplay-java-seed.g8    
    
1. Create a new application using the OntoPlay Java template:
```
sbt new mdrozdo/ontoplay-java-seed.g8
```

Note that the seed requests the OntoPlay branch/tag to be downloaded in the next step. 

The script creates a new folder with the name given during its initialization. After the script completes, change directory to the newly created folder and proceed with the next step.

2. Run the following script to download the OntoPlay sources into the ontoplay subfolder:
```
sbt getOntoPlay
```

3. At this point you should have a sample Play application and a /ontoplay subfolder, including the full OntoPlay repository contents.
4. It should now be possible to access the functionality, for example by running the application (from an IDE or using `sbt run`) and pointing the browser to: `http://localhost:9000/add/ClassName`
5. The OntoPlay config file is defined in the conf/application.conf file:
```
ontoplay.config = "conf/ontoplay.conf"
```
The ontoplay.conf file should contain the information about the ontology that is to be used by the system. The required properties are as follows:
```
filePath="configuration/uploads/TAN.owl"  # path to the ontology file
ontologyNamespace="http://www.tan.com"    # the namespace of the root ontology contained in the ontology file
checkFilePath="configuration/uploads/TANCheckk.owl" # the path to the check ontology file, that is used for integrity verification
originalAnnotationsFilePath="configuration/AnnotationCFOriginal.xml"  # the path to the file containing the default annotation configuration
annotationsFilePath="configuration/AnnotationCF.xml"  # the path to the file containing annotation configuration -- can be copied from the default annotation file
uploadsPath="configuration/uploads/"  # path to a folder that will be used when user uploads an ontology
fileMappings={
  # "http://some.ontology.org/" : "file:onto/ontology.owl"  # mappings of ontology URIs to file paths. Necessary if the ontology has imports and we would like to serve them from our server.
}
```


### Adding to existing application

To add OntoPlay to an existing Play application, you should follow the steps, adapted from the excellent post -- [How To Create A Module With Play Framework](https://luiscamilo.com/2015/07/26/how-to-create-a-module-with-play-framework/#module/sub-project). 

1. Download the OntoPlay sources and put them in a subfolder called ontoplay.
If you use Git, you might consider adding the OntoPlay repository as a Git submodule or Git subtree.

2. At this point you should have an empty Play application and a /ontoplay subfolder, including the full OntoPlay repository contents.
3. You need to add the ontoplay module to the build.sbt file in the main application's folder: 

```scala
lazy val module = (project in file("ontoplay"))
  .enablePlugins(PlayJava)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .aggregate(module)
  .dependsOn(module)
```
4. Modify the OntoPlay Guice module in the main app/Module.java file: 
* add an injected constructor
```java
private final Environment environment;
private final Configuration configuration;

public Module(Environment environment, Configuration configuration) {
    this.environment = environment;
    this.configuration = configuration;
}
```
* initialize the ontoplay module in the configure method

```java
install(new ontoplay.Module(new play.Environment(environment.underlying()), new play.Configuration(configuration.underlying())));
bind(MainTemplate.class).to(OntoPlayMainTemplate.class);
```
5. Add the OntoPlay routes to the conf/routes file:
```
#Add ontoplay routes
->         /                           ontoplay.Routes
```
6. Finally, add configuration of the module to the conf/application.conf file:
```
ontoplay.config = "conf/ontoplay.conf"
```
The ontoplay.conf file should contain the information about the ontology that is to be used by the system. The required properties are as follows:
```
filePath="configuration/uploads/TAN.owl"  # path to the ontology file
ontologyNamespace="http://www.tan.com"    # the namespace of the root ontology contained in the ontology file
checkFilePath="configuration/uploads/TANCheckk.owl" # the path to the check ontology file, that is used for integrity verification
originalAnnotationsFilePath="configuration/AnnotationCFOriginal.xml"  # the path to the file containing the default annotation configuration
annotationsFilePath="configuration/AnnotationCF.xml"  # the path to the file containing annotation configuration -- can be copied from the default annotation file
uploadsPath="configuration/uploads/"  # path to a folder that will be used when user uploads an ontology
fileMappings={
  # "http://some.ontology.org/" : "file:onto/ontology.owl"  # mappings of ontology URIs to file paths. Necessary if the ontology has imports and we would like to serve them from our server.
}
```
7. It should now be possible to access the functionality, for example by running the application (from an IDE or using `sbt run`) and pointing the browser to: `http://localhost:9000/add/ClassName`


## License

This software is licensed under the Apache 2 license - see the [LICENSE](LICENSE) file for details

