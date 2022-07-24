# Nexus Functional Tests

This repo has different test scenarios to verify our nexus instance is working as desired. 

## Running the tests

Configure the clientURL to your Nexus instance. To run locally, just run thedocker command to
download a local Nexus:
```shell
docker run -d -p 8081:8081 --name nexus sonatype/nexus3
```

Note: If is the first time running Nexus, you will need to change the admin password.

To run the test from the command line, execute the following command:

```shell
mvn clean test
```
