#!/bin/sh


svn update
mvn clean package
mv target/dj-gouda-push-server-1.0-SNAPSHOT ../

