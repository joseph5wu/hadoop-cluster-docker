#!/bin/bash

# test the hadoop cluster by running wordcount

HADOOP_JAR_PREFIX=/usr/local/hadoop/share/hadoop

rm *.class
# compile .java
# docker exec master javac -classpath /usr/local/hadoop/share/hadoop/common/hadoop-common-2.7.2.jar:/usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.2.jar:/usr/local/hadoop/share/hadoop/common/lib/commons-cli-1.2.jar ChainWordCountDriver.java
javac -classpath $HADOOP_JAR_PREFIX/common/hadoop-common-2.7.2.jar:$HADOOP_JAR_PREFIX/mapreduce/hadoop-mapreduce-client-core-2.7.2.jar:$HADOOP_JAR_PREFIX/common/lib/commons-cli-1.2.jar ChainWordCountDriver.java
jar cf cwc.jar *.class

# create input files
mkdir input
mv chainWordCountInput input/
#echo "Hello Docker Hello Hadoop Hello Joseph" >input/file.txt

# create input directory on HDFS
hadoop fs -mkdir -p input

# put input files to HDFS
hdfs dfs -put ./input/* input
hdfs dfs -rm -r output

# run wordcount
#hadoop jar $HADOOP_PREFIX/share/hadoop/mapreduce/sources/hadoop-mapreduce-examples-2.7.2-sources.jar org.apache.hadoop.examples.WordCount input output
hadoop jar cwc.jar ChainWordCountDriver input output

# print the input files
echo -e "\ninput file.txt:"
hdfs dfs -cat input/chainWordCountInput

# print the output of wordcount
echo -e "\nwordcount output:"
hdfs dfs -cat output/part-00000
