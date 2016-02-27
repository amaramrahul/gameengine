About the Application
=====================

This is a prototype which I have built in some of my spare time and should no way be considered production-ready. There are a lot of best practices (ex. documentation, unit tests, integration tests, exception handling, logging, using external configuration files) which I'd follow in production, but not implemented here, as the focus of this prototype was more on system design + class design. Read the design.odt document to understand more about the Requirements and Design Challenges.


Running the Application (Tested on Debian with OpenJDK 8)
=========================================================

Starting the services
---------------------

(Aerospike)

Download from http://www.aerospike.com/download and install it:

$ tar -xvzf aerospike-server-community-3.7.4-ubuntu12.04.tgz
$ cd aerospike-server-community-3.7.4-ubuntu12.04/ && sudo ./asinstall

Add the following namespaces in /etc/aerospike/aerospike.conf:

namespace clientaddress {
        replication-factor 2
        memory-size 1G
        default-ttl 1d
        storage-engine memory
}
namespace groupinfo {
        replication-factor 2
        memory-size 1G
        default-ttl 1d
        storage-engine memory
}
namespace game {
        replication-factor 2
        memory-size 4G
        default-ttl 0d
        storage-engine memory
        # To use file storage backing, comment out the line above and use the
        # following lines instead.
#       storage-engine device {
#               file /opt/aerospike/data/bar.dat
#               filesize 16G
#               data-in-memory true # Store data in memory in addition to file.
#       }
}

and then start aerospike:
$ sudo service aerospike restart

(Kafka)

Download from http://kafka.apache.org/downloads.html and install it:

$ sudo tar -xvzf kafka_2.11-0.9.0.1.tgz
$ sudo mv kafka_2.11-0.9.0.1 /opt/kafka/

and start it:

$ sudo /opt/kafka/bin/zookeeper-server-start.sh -daemon /opt/kafka/config/zookeeper.properties
$ sudo /opt/kafka/bin/kafka-server-start.sh -daemon /opt/kafka/config/server.properties

(userapi service)
$ cd /path-to-codebase/userapi
$ mvn clean package
$ mvn jetty:run # starts userapi service on port 8080

(Game engine services)
$ cd /path-to-codebase/gameengine
$ mvn clean package
$ mvn jetty:run -D jetty.http.port=8081 # first game engine server
$ mvn jetty:run -D jetty.http.port=8082 # second game engine server

(Timeout monitor service)
$ cd /path-to-codebase/gameengine
$ mvn exec:java -Dexec.mainClass=com.example.gameengine.timeoutmonitor.TimeoutMonitor -Dexec.args="LuckyNumber 0"


Starting the client sessions
----------------------------

cd /path-to-codebase/client
mvn clean package
Terminal#1 : $ mvn exec:java -Dexec.mainClass=com.example.client.App -D exec.args="127.0.0.1:8081 user1"
Terminal#2 : $ mvn exec:java -Dexec.mainClass=com.example.client.App -D exec.args="127.0.0.1:8082 user2"

To ping a user, enter the command: p <userid> <msg>

To start a new group, enter the command: n <groupid>

Kindly note that the <groupId> is dummy and determines the userIds of the connected users. For ex. if you give groupId 2, it will assume that the groupId 2 has two members, whose userIds are 1 (user1) and 2 (user2).

To make a move, enter the command: m <gameid> <number>

<gameid> can be got from the response received when a new game is started. <number> is the number to guess.
