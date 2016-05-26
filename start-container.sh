#!/bin/bash

eval "$(docker-machine env default)"
# run N slave containers
N=$1

# the defaut node number is 3
if [ $# = 0 ]
then
	N=5
fi

MASTER_IMAGE=yiw376/hadoop-master
SLAVE_IMAGE=yiw376/hadoop-slave
HOST_POSTFIX=yiw376.eng.ucsd.edu
MASTER_HOST=master.yiw376.eng.ucsd.edu
MASTER_CONTAINER=master
SLAVE_CONTAINER_PREFIX=slave

# delete old master container and start new master container
docker rm -f master &> /dev/null
echo "start master container..."
docker run -d -t --dns 127.0.0.1 -P --name $MASTER_CONTAINER -h $MASTER_HOST -w /root $MASTER_IMAGE &> /dev/null

# get the IP address of master container
FIRST_IP=$(docker inspect --format="{{.NetworkSettings.IPAddress}}" master)

# delete old slave containers and start new slave containers
i=1
while [ $i -lt $N ]
do
	docker rm -f $SLAVE_CONTAINER_PREFIX$i &> /dev/null
	echo "start slave$i container..."
	docker run -d -t --dns 127.0.0.1 -P --name $SLAVE_CONTAINER_PREFIX$i -h $SLAVE_CONTAINER_PREFIX$i.$HOST_POSTFIX -e JOIN_IP=$FIRST_IP $SLAVE_IMAGE &> /dev/null
	i=$(( $i + 1 ))
done


# create a new Bash session in the master container
docker exec -it $MASTER_CONTAINER bash
