@ECHO OFF

IF "%MAVEN_OPTS%" == "" (
    ECHO The environment variable 'MAVEN_OPTS' is not set, setting it for you
    SET MAVEN_OPTS=-Xms256m -Xmx2G -XX:PermSize=300m
)

SET MAVEN_OPTS=-Xms256m -Xmx2G -XX:PermSize=300m -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n
ECHO MAVEN_OPTS is set to '%MAVEN_OPTS%'
mvn clean install -Pamp-to-war -s "C:\apache-maven-3.2.3\conf\settings-salvo.xml" -Dmaven.tomcat.port=8083 -Dspring.profiles.active="amqp" 
