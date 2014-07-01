#!/bin/bash
SERVER_HOME=`dirname $0`
LIB_DIR=$SERVER_HOME/../lib
TMP_DIR=/tmp
SERVER_OPTS="-Xms512M -Xmx4096M -Denv=Pro"
APP_NAME="dajie-gouda-push-broker-server"
COMMANDS=(start,stop,restart,help,exit)
CLASSPATH=.:$SERVER_HOME/../conf:$JAVA_HOME/lib/tools.jar
GCOPT="-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:../logs/gc.out"
OPTION=$1

if [ $# -lt 1 ] ; then
 echo please input the correct command "[" ${COMMANDS[@]} "]"
 read -p ">"  command
 OPTION=$command
fi

helpinfo(){
   echo options include  "[" ${COMMANDS[@]} "]"
   exit 0
}


check_run(){
    pid=$1

    if [ -z "$run_pid" ]
    then
        echo mqtt_broker start fail 
    else
        echo mqtt_broker start succ : $pid
    fi

}

startbroker(){
  
   for jar in `ls $LIB_DIR/*.jar`
   do
      CLASSPATH=$CLASSPATH:$jar
   done
   
   $JAVA_HOME/bin/java $GCOPT $SERVER_OPTS -Djava.io.tmpdir=$TMP_DIR  -Dapp.name=$APP_NAME  -cp $CLASSPATH com.dajie.push.Broker  &

   run_pid=`ps aux|grep $APP_NAME|grep -v 'grep'|awk {'print $2'}`
   check_run $run_pid   
}


stopbroker(){
  PID=`ps aux|grep $APP_NAME|grep -v 'grep'|awk {'print $2'}`
  if [ ! -z $PID ] ; then
  kill -9 $PID
  echo $PID
  else
  echo 0
  fi
}

restartbroker(){
  ret=`stopbroker`
  echo :$ret
  sleep 10
  startbroker
}

case $OPTION in
start)
    startbroker
;; 
stop)
    stopbroker
;; 
restart)
    restartbroker
;;
help) 
    helpinfo
;;
quit) 
    exit 0
;;
*) 
    echo "error command"
    helpinfo
;;
esac

