# sachinCSTest


Introduction
-------
* Program takes the path to logfile.txt as an input argument
* Parse the contents of logfile.txt
* Flag any long events that take longer than 4ms
* Write the found event details to file-based HSQLDB (http://hsqldb.org/) in the working folder The
application should create a new table if necessary and store the following values:
* Event id
* Event duration
* Type and Host if applicable
* Alert (true if the event took longer than 4ms, otherwise false)


Setup
-----

* Install maven
* Install hsqldb
* Clone repository: git@github.com:sack112007/sachinCSTest.git
* Start the Database as per details provided in Config.properties file at path:src/main/resources/


Command-line Instructions
-------------------------
```bash
cd sachinCSTest/customLogs
mvn compile exec:java -Dexec.args="Your Input File.txt file"
```



