# Creates a master Hadoop image
FROM yiw376/hadoop-base

MAINTAINER Yingzhi Wu <yiw376@eng.ucsd.edu>

ADD files/run-wordcount.sh /tmp/run-wordcount.sh
ADD files/wordCount/* /tmp/wordCount/

RUN mv /tmp/wordCount ~/wordCount 

RUN mv /tmp/run-wordcount.sh ~/run-wordcount.sh && \
    chmod +x ~/run-wordcount.sh

ADD files/slaves $HADOOP_PREFIX/etc/hadoop/slaves

ADD files/bootstrap.sh /etc/bootstrap.sh
RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

ENV BOOTSTRAP /etc/bootstrap.sh

CMD ["/etc/bootstrap.sh", "-d"]
