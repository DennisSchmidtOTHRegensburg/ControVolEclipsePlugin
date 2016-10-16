# ControVol Eclipse-Plugin
## An Eclipse-Plugin for controlled Schema-Evolution in NoSQL-Datastore MongoDB/Google Datastore.

Schema-free datastores like MongoDB or Google Datastore provide high flexiblity in agile software development. In such a process the software is released early and then updated frequently.
Furthermore in professional software development objectmapper-libraries like Morphia
are used to fill the gap between the object-orientated program-language and the datastore-model.
Those objectmappers implicate a schema in the otherwise schema-free datastore.
Through frequent updates of the software big problems occur after changing the
schema resp. class-declarations (schema-evolution): After changing the class-declaration
pervious saved objects keep their schema and were then loaded with the new class-declaration.
As a result dataloss or runtime errors could occure.
This Eclipse-Plugin supports developers on controlled schema evolution with the
objectmappers Morphia for MongoDB and Objectify for Google Datatstore.
Changes made to class-declarations are compared to earlier releases of the software.
The plugin detects possible problems and offers solutions through the Eclipse QuickFix Popup.
  
There are two videos available showing possible uses cases:
- In the first video the Eclipse-Plugin detects that renaming an attribute leads to data loss. Therefore it suggests using proper annotations of the used objectmapper.
- In the second video the type of an attribute is changed from String to Integer. The plugin detects that a runtime-error will occur and suggests proper solutions. 

Detailed explanation on the annotations for controlled schema evolution were provided by the [Morphia](https://github.com/mongodb/morphia)/[Objectify](https://github.com/objectify/objectify) Wikis.

# Project description:
- **ControvolEngine:** Project for extracting and checking class declarations.
- **ControvolEclipsePlugin:** The plugin for controlled schema-evolution. This project uses the jar which is generated in the ControvolEngine-project.
- **ControvolEclipsePluginFeature**: A Project which holds a reference to the ControvolEclipsePlugin-project.
This project defines the installation-routine for the plugin. This project can be exported as a feature with the Eclipse Export Manager. The result of the export is the Feature-folder.
- **Feature-folder:** With this folder the plugin is installable with the Eclipse Update Manager.
- **MorphiaTestProject:** Test-project with the objectmapper Morphia
- **ObjectifyTestProject:** Test-project with the objectmapper Objectify

# User Guide
**Requirements:**
- Developmentenvironment: Eclipe IDE for Eclipse Committers (Tested with
Version: Mars Release 4.5.0, Mars.1 Release 4.5.1, Mars.2 Release 4.5.2)
- OS: Windows 7,8, Unix or Mac OS X (Tested with Windows 7,8, Unix)
  
**Installation and deinstallation of the plugin:**   
The plugin can be installed and deinstalled with the Eclipse Update Manager with Help>Install New Software. For installation the path to the local Feature-folder has to be stated.    
**Configure a example database-project**  
In the given example a database-project with MongoDB and Morphia is used. In the
example a guestbook-software with the class Guestbook is stated. The
plugin only considers classes in which valid Mophia/Obectify imports and annotation
are present. This means it only considers classes which objects can be stored in the
datastore with the objectmapper Morphia/Objectify.  
![projectpreparation](/docs/Projectpreparation.jpg "ControVolEclipsePlugin")  

To be able to use the functions of the plugin the project has to be configured. With the selection Convert to ControVol Project a folder called ControVol is created in the database-project.
With the selection Register current Version the current class decleration is saved in a
xml-file which will be stored in the ControVol-folder. Perhaps the project has to be
updated with *F5*. Every class and attribute which can be stored in the datastore is
listed in the xml-file.
![Configure](/docs/Configure.jpg "ControVolEclipsePlugin")  

**The controlled schema Evolution:**  
Every change in the class declarations which objects can be stored in the datastore
are now checked with the version in the xml-files. For example if an attribute of the
Guestbook-class is renamed with the refactoring-tool of Eclipse a warning is created.
Perhaps the Java-file has to be saved with *Strg+S* to make the warning visible. Now a
codefix for the waring can be selected. If the current project is declared as a release
with Register current Version a second xml-file is created. Changes made
to the class declearation are now checked with both xml-files.  
![Quickfix](/docs/Quickfix.jpg "ControVolEclipsePlugin")  

#Developer Guide
**Requirements:**
- Developmentenvironment: Eclipe IDE for Eclipse Committers (Tested with
Version: Mars Release 4.5.0, Mars.1 Release 4.5.1, Mars.2 Release 4.5.2)
- OS: Windows 7,8, Unix or Mac OS X (Tested with Windows 7,8, Unix)
- Maven installed and configured 
- JDK 1.8 installed and configured
  
**Configuration of the projects for development**  
Maven is used to download and to add the necessary libraries to the ControVolEngine-project.
Maven is also used to generate a jar-file from the ConrovolEngine-project. This jar will then be added along with other libraries to the ControvolEclipsePlugin-project. It is possible to download the necessary libraries and generating a jar without Maven.  
  
**ControvolEngine-project**  
First copy the ControvlEngine-project to the Eclipse-workspace and install and configure Maven.
With the terminal/cmd change to the path of the ControvolEngine-folder and execute
the following Maven-commands:  
  
```
mvn eclipse:eclipse
mvn compile
mvn clean install
```  
  
With the first command the .project-file will be updated. This is only necessary after the
first import of the project. The other two commands have to be executed after every
change of the code. With the second command the dependencies stated in the pom.xml
are downloaded and added to the classpath of the ControvlEngine-project and also the
target-folder with the generated jar-file is recreated. With the third command the
generated jar is copied into the local Maven-repository. Now change to Eclipse and
refresh the project by pressing F5 key. ControvlEngine should now find all necessary dependencies.  
  
**ControvolEclipsePlugin-project**  
At first copy the ControvolEclipsePlugin-project to the Eclipse-workspace.
Then go to the Eclipse IDE and delete all dependencies of the ControvolEclipsePlugin-project manually with the BuildPath-Entry and re-add them with *add external jars*. All jars are located in the lib-folder of the Plugin project (The deletion and re-adding of the imports is only necessary after the first import). Now the ControVolEclipsePlugin-project should be without any errors.
If the code of the ControvolEngine-project has changed the jar has to be generated new. (Either manually or with the Maven-commands.)
Then copy the gernerated ControVolEngine-1.0-SNAPSHOT.jar from the target-folder
or the manually generated jar into the lib-folder of the ControVolEclipsePlugin-project.
After that refresh the project by pressing the F5-key.
The plugin can now be executed with *Run As> Eclipse Application*. A second instance
of Eclipse is opened. In this instance the Plugin is installed and can be tested. Import
the TestProjectMorphia and TestProjcetObjecify Projects into the workspace of the new Eclipse-instance which is called . In the Testprojects open the Java Build Path and delete the dependencies and re-add them with *add external jars*. The dependencies are in the lib-folders of the projects. Then continue as descripted in the User Guide.
