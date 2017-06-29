# OntoPlay

OntoPlay is a flexible interface for ontology-based systems. OntoPlay allows creation of dynamical interfaces that allow ontology-illiterate users to select a class or an individual on the basis of the ontology of the system. Furthermore, the OntoPlay is ontology agnostic and allows for ontology modification without the need for changing the code of the user interface.

The module has been described in several articles:
* [OntoPlay - A Flexible User-Interface for Ontology-based Systems](http://ceur-ws.org/Vol-918/111110086.pdf)
* [Graphical Interface for Ontology Mapping with Application to Access Control](https://link.springer.com/chapter/10.1007/978-3-319-54472-4_5)
* [Implementing Agent-based Resource Management in Tsunami Modeling - Preliminary Considerations](http://www.ibspan.waw.pl/~paprzyck/mp/cvr/research/AiG_papers/DNIS2014.pdf)


## Getting Started

OntoPlay is a [Play Framework](https://playframework.com/) submodule. It is meant to be included in a top level Play application, in the same way as it is used in the [OntoPlay-TAN](https://github.com/mdrozdo/OntoPlay-TAN) project. 

### Prerequisites

The project requires installation of the Play Framework (see instructions [here](https://playframework.com/documentation/2.5.x/Installing)).

### Installing

To create a new application using OntoPlay, you should follow the steps, adapted from the excellent post -- [How To Create A Module With Play Framework](https://luiscamilo.com/2015/07/26/how-to-create-a-module-with-play-framework/#module/sub-project). In step 2 we assume adding OntoPlay from GitHub using [git subtree](https://medium.com/@porteneuve/mastering-git-subtrees-943d29a798ec), however it's equally possible to simply download the code and copy it into the correct subfolder.

1. Create a new Play application, e.g. using the play java template:
```
sbt new playframework/play-java-seed.g8
```
2. Add the OntoPlay repository as a subtree to your git repo.
```
git init
git remote add ontoplay https://github.com/mdrozdo/OntoPlay.git
git subtree add --prefix=ontoplay --squash ontoplay master
```
3. At this point you should have an empty Play application and a /ontoplay subfolder, including the full OntoPlay repository contents.
4. You need to add the ontoplay module to the build.sbt file in the main application's folder: 

```scala
lazy val module = (project in file("ontoplay"))
  .enablePlugins(PlayJava)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .aggregate(module)
  .dependsOn(module)
```
5. Add initialization of the OntoPlay Guice module in the main app/Module.java file:
```java
install(new ontoplay.Module(new play.Environment(environment.underlying()), new play.Configuration(configuration.underlying())));
bind(MainTemplate.class).to(OntoPlayMainTemplate.class);
```
6. Add the OntoPlay routes to the conf/routes file:
```
#Add ontoplay routes
->         /                           ontoplay.Routes
```
7. Finally, add configuration of the module to the conf/application.conf file:
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
8. It should now be possible to access the functionality, for example by running the application (from an IDE or using `sbt run`) and pointing the browser to: `http://localhost:9000/add/ClassName`


## License

This software is licensed under the Apache 2 license - see the [LICENSE](LICENSE) file for details

