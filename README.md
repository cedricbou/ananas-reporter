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

All configuration is done in an external configuration file. The configuration file is loaded with by setting the property `config.file`.

For example : 

```
$ java -Dconfig.file=application.conf -jar ananas-reporter-all.jar
```

Ananas Reporter is using the [TypeSafe Config](https://github.com/typesafehub/config) library to load configuration file. It allows other loading method such as the system property `config.url`. It also propose a very fine syntax. The config project has a README.md really well written and explained, you should refer to it for further details on the configuration syntax and loading possibilities.

### Configuring datasources

Because a report is the result from a query, we need to configure the databases that the query will use.

Ananas Reporter is using Spring [DriverManagerDataSource](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/jdbc/datasource/DriverManagerDataSource.html) to connect to a database if no jdbc Driver class is specified. Otherwise, it uses the Spring [SimpleDriverDataSource](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/jdbc/datasource/SimpleDriverDataSource.html) when a jdbc driver class is specified.

The connection is established before running a report, and closed after the query has finished.

To configure a datasource, create a named datasource object inside the `datasources` root configuration element :

```
datasources {
	// This will create a connection a localhost mysql using root user.
	// The url must be a standard JDBC url.
	//
	// Because a driver is specified, it will use 
	// the Spring DriverManagerDataSource internally.
	asampledb: {
		url: "jdbc:mysql://localhost/base1"
		driver: "com.mysql.jdbc.Driver"
		user: "root"
		password: ""
	}

	// This will create a connection a localhost mysql using root user.
	// Because no driver is specified, it will use 
	// the Spring SimpleDriverDataSource internally.
	anothersample: {
		url: "jdbc:mysql://localhost/base2?user=root&password="
	}
}
```

### Configuring emailer

The `emailer` object is their for smtp configuration. For now it supports only one configuration :

```
emailer {
   smtp: "smtp.example.com"
}
```

There is place for improvement here with support for smtp with authentification and TLS.

### Configuring reports

The `reports` object contains the report you will configure. A report is referring to a declared datasource, it define a SQL query and all data used to send the email (from/to/subject).

```
reports {
    // You can defined any name for your report as long 
    // as it respect naming convention from TypeSafe config.
	myFirstReport {
		// The datasource name, as declared in the `datasources` configuration object.
		ds: "base1" 
		
		// The SQL query that will be executed, results are transformed into a CSV file and send by email.
		query: "select left(creation_date, 7) as monthly, count(*) as countPerMonth from customers group by monthly"

		// The email recipient addresses, one or several. 
		to: ["youremail@example.com", "anotheremail@example.com"]
		
		// The email sender address.
		from: "senderemail@example.com"
		
		// The email subject, you can use date/time format to include
		// temporal notion in your subject.
		subject: "customer count on %d{YYYY, MMMM}"

		// The cron expression, this defines when the report will be executed.
		cron: "* */5 * * * *"
	}
}
```

- `ds` : This should contain the name of the datasource to use. The datasource must have been declared in the `datasources` object with the same name.
- `query` : The SQL query to use in this report. In the report, column will be sorted by alphabetical order. You can prefix column names with number if you want control on the column order. For example : `select id as 0_id, name as 2_name, address as 1_address ...`. This will create three columns in the CSV file in the following order : `0_id, 1_address, 2_name`.
- `to` : A list of email addresses. They will all receive the report.
- `from` : The email address of the sender of the report. Usually this should be some kind of no-reply address such `no-reply@example.com`.
- `subject` : The email subject. You can add date pattern inside using the syntax `%d{...}`. The patter inside the brackets must follow [Joda-Time DateTimeFormatter](http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html) syntax.
- `cron` : A cron expression backed by Spring Scheduler/CronTrigger. The cron expression must follow 6 fields (with seconds) cron convention. You can refer to this [documentation](http://quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger) to understand cron expression. 

## Providing custom LogBack configuration

Ananas Reporter does not log much for the moment. It uses Slf4j/Logback for logging. By default there is no configuration so everything will be printed on `STDOUT`.

You can provide your own `logback.xml` configuration file by using the system property `logback.configurationFile`. For example :

```
$ java -Dlogback.configurationFile=mylogback.xml -Dconfig.file=application.conf -jar ananas-reporter-all.jar
```

More information on configuring Logback can be found [here](http://logback.qos.ch/manual/configuration.html).

## Providing third party jdbc driver

Ananas Reporter has a dependency for MySQL driver. If you need other JDBC drivers, they should be added to the classpath :

- Create a directory where the `ananas-reporter-all.jar` file is located, for example 'drivers'. 
- Put required jdbc drivers jar in it.
- Run Ananas Reporter with the following command :

```
$ java -Dconfig.file=application.conf -cp "ananas-reporter-all.jar;drivers/*" com.emo.ananas.app.App
```

Then configure your `datasources` as described in this documentation.

## Why such a weird name : Ananas Reporter ?

I've decided to call all my projects with fruits name. It's fresh and good. There is no specific reason but I had just eaten an ananas when I had the idea for this project ;-)
