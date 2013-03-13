Ananas reporting is short java program to send directly through email the result of a SQL query at programmed time (cron like). It is based on a configuration file.

## How To Get Started

- You need the JDK6 and maven 3 to build the source.
- Download the source from github.
- run '''mvn package''.
- Put the jar in a directory.
- Copy the sample application.conf sample next to the jar file.
- Edit the application.conf file to your needs.
- Run with ''java -Dconfig.file=application.conf -jar ananas-reporting.jar''

## Configuring reports

### Configuring datasources

### Configuring emailer

### Configuring reports

## Providing custom LogBack configuration

## Providing third party jdbc driver
