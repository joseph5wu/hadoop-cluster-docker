# Creates a slave Hadoop image
FROM yiw376/hadoop-base

MAINTAINER Yingzhi Wu <yiw376@eng.ucsd.edu>

ADD files/bootstrap.sh /etc/bootstrap.sh
RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

ENV BOOTSTRAP /etc/bootstrap.sh

CMD ["/etc/bootstrap.sh", "-d"]
