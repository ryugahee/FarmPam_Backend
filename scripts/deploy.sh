#!/bin bash

REPOSITORY=/home/runner/work/FarmPam_Backend/FarmPam_Backend
cd $REPOSITORY

# jar 파일 찾기
APP_NAME=cicdproject
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar')
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

# jar 파일 실행
CURRENT_PID=$(pgrep -f $APP_NAME)

# 해당 프로세스가 실행 되었을 시 종료
if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 새 버전의 프로세스를 실행
echo "> $JAR_PATH 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &