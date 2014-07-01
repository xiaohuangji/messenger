#!/bin/bash

if [[ $1 == "-server" ]]; then
    SERVER=$2
    echo $SERVER
fi

start(){
   $JAVA_HOME/bin/java   -Djava.ext.dirs=../lib  com.dajie.push.BrokerClient $SERVER
}

start



