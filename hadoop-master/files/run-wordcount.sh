#!/bin/bash

# test the hadoop cluster by running wordcount

HADOOP_JAR_PREFIX=/usr/local/hadoop/share/hadoop

# compile .java
javac -classpath $HADOOP_JAR_PREFIX/common/hadoop-common-2.7.2.jar:$HADOOP_JAR_PREFIX/mapreduce/hadoop-mapreduce-client-core-2.7.2.jar:$HADOOP_JAR_PREFIX/common/lib/commons-cli-1.2.jar WordCount2.java
jar cf wc.jar WordCount2*.class

# create input files
mkdir input
#echo "Hello Docker Hello Hadoop Hello Joseph" >input/file.txt
echo -e "Hello Apple Docker \nBee Cee" >input/file1.txt
echo -e "Hello Apple Hadoop \nBee Cee" >input/file2.txt

# create input directory on HDFS
hadoop fs -mkdir -p input

# put input files to HDFS
hdfs dfs -put ./input/* input
hdfs dfs -rm -r output

# run wordcount
#hadoop jar $HADOOP_PREFIX/share/hadoop/mapreduce/sources/hadoop-mapreduce-examples-2.7.2-sources.jar org.apache.hadoop.examples.WordCount input output
hadoop jar wc.jar WordCount2 input output

# print the input files
#echo -e "\ninput file.txt:"
#hdfs dfs -cat input/file.txt

echo -e "\ninput file1.txt:"
hdfs dfs -cat input/file1.txt

echo -e "\ninput file2.txt:"
hdfs dfs -cat input/file2.txt

# print the output of wordcount
echo -e "\nwordcount output:"
hdfs dfs -cat output/part-r-00000
