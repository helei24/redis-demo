#!/bin/sh
#chkconfig: 345 86 14
#description: Startup and shutdown script for Redis
 
PROGDIR=/home/integrasco/Software/redis-2.8.17 #安装路径
PROGNAME=redis-server
REDIS_CLI=redis-cli
DAEMON=$PROGDIR/src/$PROGNAME
CONFIG=/etc/redis.conf
PIDFILE=/var/run/redis.pid
DESC="redis daemon"
SCRIPTNAME=/etc/rc.d/init.d/redisd
 
start()
{
  if test -x $DAEMON
         then
              echo "Starting $DESC: $PROGNAME"
              if $DAEMON $CONFIG
              then
                echo "OK"
              else
                echo "failed"
              fi
         else
            echo "Couldn't find Redis Server ($DAEMON)"
         fi       
}
 
stop()
{
  NUM=`ps aux | grep $PROGNAME | wc -l`
  if [ $NUM -ge 2 ]; then
      if $PROGDIR/src/$REDIS_CLI shutdown
      then
          echo "OK"
      else
          echo "failed"
      fi
  else
      echo "No Redis Server ($DAEMON) running"  
          
  # if test -e $PIDFILE
  # then
  #   echo -e "Stopping $DESC: $PROGNAME"
  #   if kill `cat $PIDFILE`
  #   then
  #     echo -e "OK"
  #   else
  #     echo -e "failed"
  #  fi
  # else
  #   echo -e "No Redis Server ($DAEMON) running"
  fi
}
 
restart()
{
    echo "Restarting $DESC: $PROGNAME"
    stop
    start
}
 
status()
{
    NUM=`ps aux | grep $PROGNAME | wc -l`
    if [ $NUM -ge 2 ]; then
        echo "Redis Server ($DAEMON) is running"
    else
        echo "No Redis Server ($DAEMON) running"    
    fi    
}
 
case $1 in
        start)
            start
        ;;
        stop)
            stop
        ;;
        restart)
            restart
        ;;
        status)
            status
        ;;
         *)
        echo "Usage: $SCRIPTNAME {start|stop|restart|status}" >&2
        exit 1
        ;;
esac
exit 0
