@echo off
color 02
FOR /F "delims==" %%i IN ((find  "updateDate" config.properties)) DO echo %updateDate%
rem find  "updateDate" config.properties
title=svn更新上传至服务器-by wangyl
rem java -jar svn.jar
pause