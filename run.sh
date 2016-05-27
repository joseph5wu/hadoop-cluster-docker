#! /bin/bash

USERNAME=yiw376
SERF_DNSMASQ_IMAGE=$USERNAME/serf-dnsmasq
BASE_IMAGE=$USERNAME/hadoop-base
MASTER_IMAGE=$USERNAME/hadoop-master
SLAVE_IMAGE=$USERNAME/hadoop-slave
MASTER_CONTAINER=master
SLAVE_CONTAINER_PREFIX=slave
HOST_POSTFIX=$USERNAME.eng.ucsd.edu
MASTER_HOST=master.$HOST_POSTFIX
DNS=127.0.0.1

# run N slave containers
N=$1
# the defaut node number is 5
if [ $# = 0 ]
then
	N=5
fi

echo "### Step0: Build virtual machine to run our test ###"
# docker-machine create --driver virtualbox $USERNAME
eval "$(docker-machine env default)"

echo "### Step1: Build images ###"
docker build -t $SERF_DNSMASQ_IMAGE serf-dnsmasq
docker build -t $BASE_IMAGE hadoop-base
docker build -t $MASTER_IMAGE hadoop-master
docker build -t $SLAVE_IMAGE hadoop-slave

echo "### Step2: Set up master container ###"
# delete old master container and start new master container
docker stop master &> /dev/null
docker rm -f master &> /dev/null
docker run -d -t --dns $DNS -P --name $MASTER_CONTAINER -h $MASTER_HOST -w /root $MASTER_IMAGE &> /dev/null

# get the IP address of master container
MASTER_IP=$(docker inspect --format="{{.NetworkSettings.IPAddress}}" $MASTER_CONTAINER)

echo "### Step3: Set up 4 slave containers ###"
# delete old slave containers and start new slave containers
i=1
while [ $i -lt $N ]
do
    docker stop $SLAVE_CONTAINER_PREFIX$i &> /dev/null
	docker rm -f $SLAVE_CONTAINER_PREFIX$i &> /dev/null
	echo "start slave$i container..."
	docker run -d -t --dns $DNS -P --name $SLAVE_CONTAINER_PREFIX$i -h $SLAVE_CONTAINER_PREFIX$i.$HOST_POSTFIX -e JOIN_IP=$MASTER_IP $SLAVE_IMAGE &> /dev/null
	i=$(( $i + 1 ))
done

sleep 5
echo "### Step4: Start hadoop in master container ###"
# start hadoop in master container
docker exec master ./start-hadoop.sh

sleep 15
echo "### Step5: Run word count test ###"
# run word count
docker exec master ./run-wordcount.sh
