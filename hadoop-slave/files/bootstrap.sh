#!/bin/bash
: ${HADOOP_PREFIX:=/usr/local/hadoop}

$HADOOP_PREFIX/etc/hadoop/hadoop-env.sh

rm /tmp/*.pid

# installing libraries if any - (resource urls added comma separated to the ACP system variable)
cd $HADOOP_PREFIX/share/hadoop/common ; for cp in ${ACP//,/ }; do  echo == $cp; curl -LO $cp ; done; cd -

# start sshd
echo "start sshd..."
service ssh start

# start sef
echo -e "\nstart serf..."
/etc/serf/start-serf-agent.sh > serf_log &

sleep 10

serf members

nohup $HADOOP_PREFIX/bin/hdfs datanode 2>> /var/log/hadoop/datanode.err >> /var/log/hadoop/datanode.out &
nohup $HADOOP_PREFIX/bin/yarn nodemanager 2>> /var/log/hadoop/nodemanager.err >> /var/log/hadoop/nodemanager.out &

if [[ $1 == "-d" ]]; then
    while true; do sleep 1000; done
fi

if [[ $1 == "-bash" ]]; then
    /bin/bash
fi
