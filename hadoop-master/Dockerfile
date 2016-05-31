# Creates a master Hadoop image
FROM yiw376/hadoop-base

MAINTAINER Yingzhi Wu <yiw376@eng.ucsd.edu>

ADD files/run-wordcount.sh /tmp/run-wordcount.sh
ADD files/run-chainwordcount.sh /tmp/run-chainwordcount.sh
ADD files/reset_for_rerun.sh /tmp/reset_for_rerun.sh
ADD files/WordCount2.java /tmp/WordCount2.java
ADD files/ChainWordCountDriver.java /tmp/ChainWordCountDriver.java
ADD files/chainWordCountInput /tmp/chainWordCountInput

RUN mv /tmp/WordCount2.java ~/WordCount2.java && \
    mv /tmp/ChainWordCountDriver.java ~/ChainWordCountDriver.java && \
    mv /tmp/chainWordCountInput ~/chainWordCountInput

RUN mv /tmp/reset_for_rerun.sh ~/ && \
    chmod +x ~/reset_for_rerun.sh

RUN mv /tmp/run-chainwordcount.sh ~/ && \
    chmod +x ~/run-chainwordcount.sh

RUN mv /tmp/run-wordcount.sh ~/run-wordcount.sh && \
    chmod +x ~/run-wordcount.sh

ADD files/slaves $HADOOP_PREFIX/etc/hadoop/slaves

ADD files/bootstrap.sh /etc/bootstrap.sh
RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

ENV BOOTSTRAP /etc/bootstrap.sh

CMD ["/etc/bootstrap.sh", "-d"]
