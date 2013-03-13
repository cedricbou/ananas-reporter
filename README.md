Ananas reporting is short java program to send directly through email the result of a SQL query at programmed time (cron like). It is based on a configuration file.

## How To Get Started

- You need the JDK6 and maven 3 to build the source.
- Download the source from github.
- run '''mvn package''.
- From the target directory, copy ananas-reporter-all.jar to a directory of your choice.
- Copy the sample [application.conf](https://github.com/cedricbou/ananas-reporter/blob/master/src/main/config/application.conf) in the same directoy as the jar.
- Edit the application.conf file to suit your needs.
- From the directory containing the jar, run ''java -Dconfig.file=application.conf -jar ananas-reporter-all.jar''

## Configuring reports

### Configuring datasources

### Configuring emailer

### Configuring reports

## Providing custom LogBack configuration

## Providing third party jdbc driver
