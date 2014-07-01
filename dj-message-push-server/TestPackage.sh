#!/bin/sh

svn update
mvn clean package -P test
mv target/dj-gouda-push-server-1.0-SNAPSHOT ../

