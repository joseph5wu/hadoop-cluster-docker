#!/bin/bash

# test the hadoop cluster by running wordcount

# create input files
mkdir input
echo "Hello Docker Hello Hadoop Hello Joseph" >input/file.txt

# create input directory on HDFS
hadoop fs -mkdir -p input

# put input files to HDFS
hdfs dfs -put ./input/* input
hdfs dfs -rm -r output

# run wordcount
hadoop jar $HADOOP_PREFIX/share/hadoop/mapreduce/sources/hadoop-mapreduce-examples-2.7.2-sources.jar org.apache.hadoop.examples.WordCount input output

# print the input files
echo -e "\ninput file.txt:"
hdfs dfs -cat input/file.txt

# print the output of wordcount
echo -e "\nwordcount output:"
hdfs dfs -cat output/part-r-00000
