# Creates a master Hadoop image
FROM yiw376/hadoop-base

MAINTAINER Yingzhi Wu <yiw376@eng.ucsd.edu>

ADD files/run-bigramCount.sh /tmp/run-bigramCount.sh
ADD files/bigramCount/* /tmp/bigramCount/

RUN mv /tmp/bigramCount ~/bigramCount 

RUN mv /tmp/run-bigramCount.sh ~/run-bigramCount.sh && \
    chmod +x ~/run-bigramCount.sh

ADD files/slaves $HADOOP_PREFIX/etc/hadoop/slaves

ADD files/bootstrap.sh /etc/bootstrap.sh
RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

ENV BOOTSTRAP /etc/bootstrap.sh

CMD ["/etc/bootstrap.sh", "-d"]
