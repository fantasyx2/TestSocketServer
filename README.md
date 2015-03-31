# TestSocketServer
TestSocketServer


git remote add origin https://github.com/fantasyx2/TestSocketServer
git push -u origin master


```
java -jar FTServer.jar 8001 /Users/zhongduan-mini/Documents/FTServer/storage/
参数含义：
表示：运行服务端 java -jar FTServer.jar   端口    存储接收文件的目录（注意最后加一个/）

java -jar FTClient.jar localhost 8001 /Users/zhongduan-mini/Documents/FTServer/send/ test.txt /Users/zhongduan-mini/Documents/FTServer/storage/
参数含义：
表示：运行服务端 java -jar FTClient.jar   服务器地址（IP）   端口    要发送的文件的目录（注意最后加一个/）   要发送的文件名     存储接收文件的目录（注意最后加一个/）
```
