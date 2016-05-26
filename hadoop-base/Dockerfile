# Creates a base Hadoop image with openjdk-8
FROM yiw376/serf-dnsmasq

MAINTAINER Yingzhi Wu <yiw376@eng.ucsd.edu>

# install openssh-server, vim and openjdk
RUN apt-get update -y
RUN apt-get install -y openssh-server vim
RUN apt-get install -y openjdk-8-jdk

# move all configuration files into container
ADD files/* /usr/local/

# set jave environment variable
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $PATH:$JAVA_HOME/bin

#configure ssh free key access
RUN mkdir /var/run/sshd && \
    ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && \
    mv /usr/local/ssh_config ~/.ssh/config && \
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd

# Install Hadoop 2.7.2
RUN curl -s http://apache.spinellicreations.com/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz | tar -xz -C /usr/local/ && \
    ln -s /usr/local/hadoop-2.7.2 /usr/local/hadoop && \
    mv /usr/local/bashrc ~/.bashrc && \
    mv /usr/local/hadoop-env.sh /usr/local/hadoop/etc/hadoop/hadoop-env.sh
