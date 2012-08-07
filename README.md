<h3>Summary</h3>

This project contains a couple of examples how to call REST API services. The following technologies are used:

- Jersey :: JAX-RS :: http://jersey.java.net/
- Jackson :: JSON Processor :: http://jackson.codehaus.org/

<h3>Requirements</h3>

The source code contains example REST clients for Artifactory and Nexus. 
Both are artifact repositories and provide REST API's.

<h4>Artifactory Installation</h4>

- Unix :: http://wiki.jfrog.org/confluence/display/RTF/Installing+on+Un*x
- Windows :: http://wiki.jfrog.org/confluence/display/RTF/Installing+on+Windows

<h4>Nexus Installation</h4>

Please follow the instructions outlined in this blog article: 
http://blog.codecentric.de/en/2012/08/tutorial-create-a-jenkins-plugin-to-integrate-jenkins-and-nexus-repository/#part2

The example partially uses features that are only available in the Nexus Pro Edition.
You can also just comment out the section in the NexusTestClient.java that uses the metadata-plugin.

<h3>Project structure</h3>



```
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── de
    │   │       └── mb
    │   │           └── rest
    │   │               ├── artifactory
    │   │               │   ├── client
    │   │               │   │   └── ArtifactoryTestClient.java
    │   │               │   └── resource
    │   │               │       └── Builds.java
    │   │               └── nexus
    │   │                   ├── client
    │   │                   │   └── NexusTestClient.java
    │   │                   └── resource
    │   │                       └── RepositoryTargets.java
    │   └── resources
    └── test
        ├── java
        └── resources
```