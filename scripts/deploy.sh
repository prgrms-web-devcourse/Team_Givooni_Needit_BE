#!/bin/bash

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=needit

BUILD_JAR=$(ls /home/ec2-user/app/build/libs/*.jar)

echo "> build 파일 복사" >> /home/ec2-user/app/deploy.log

cp $BUILD_JAR $REPOSITORY/

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/app/deploy.log

CURRENT_PID=$(pgrep -fl $PROJECT_NAME | awk '{print $1}')

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/app/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포" >> /home/ec2-user/app/deploy.log

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME" >> /home/ec2-user/app/deploy.log

echo "> $JAR_NAME 에 실행권한 추가" >> /home/ec2-user/app/deploy.log

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행" >> /home/ec2-user/app/deploy.log

source /etc/profile

nohup java -jar \
  -Dspring.config.location=classpath:/application.yml \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
