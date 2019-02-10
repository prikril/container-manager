# Container Manager
Container Manager is a JavaFX UI for managing virtual LXD container networks.


## Dependencies
Java 8 or newer with JavaFX support.

LXD installation with a user generated certificate.

## Building
Use IntelliJ to open the project.

You can just run the "Main" configuration to start the application.

Use Build -> Build Artifacts to build a .jar file.

You will find the file in the "out" directory.


## Configuration
Copy example.config to default.config to set your own configuration.

Set the paths to your .crt and .key files.

Download a LXD image and give it an alias, e.g.:

``lxc image copy ubuntu:14.04 local : --alias ubuntu14``

Set the alias name in your default.config file.