# Creates a master Hadoop image
FROM yiw376/hadoop-base

MAINTAINER Yingzhi Wu <yiw376@eng.ucsd.edu>

ADD files/wordCount.sh /tmp/wordCount.sh
ADD files/bigramCount.sh /tmp/bigramCount.sh
ADD files/bigramCount/* /tmp/bigramCount/

RUN mv /tmp/bigramCount ~/bigramCount && \
    mv /tmp/wordCount.sh ~/wordCount.sh && \
    chmod +x ~/wordCount.sh && \
    mv /tmp/bigramCount.sh ~/bigramCount.sh && \
    chmod +x ~/bigramCount.sh

ADD files/slaves $HADOOP_PREFIX/etc/hadoop/slaves
ADD files/bootstrap.sh /etc/bootstrap.sh
RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

ENV BOOTSTRAP /etc/bootstrap.sh

CMD ["/etc/bootstrap.sh", "-d"]
