#! /bin/bash

echo "stop all containers"
docker stop master slave1 slave2 slave3 slave4

echo "remove all containers"
docker rm master slave1 slave2 slave3 slave4

echo "remove all images"
docker rmi yiw376/hadoop-slave yiw376/hadoop-master yiw376/hadoop-base yiw376/serf-dnsmasq
