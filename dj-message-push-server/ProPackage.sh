#!/bin/sh

#after package, please must remember to config public_ip in mqtt.properties.
#

svn update
mvn clean package -P pro
mv target/dj-gouda-push-server-1.0-SNAPSHOT ../

