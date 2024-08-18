
## SMTP Setup

### Account Setting
https://support.google.com/accounts/answer/185833#zippy=%2Cwhy-you-may-need-an-app-password
### Create App Password
https://myaccount.google.com/apppasswords?rapt=AEjHL4Mu0W7MVeqx15umkEPO5McivQ3nj-YXTgA-nsYeOFNg28V9G8pkhaAZPF5wS5aUXEaZb4oMKJI6zwNFMV4Zq28r96fI6saIz1JN3WuwvcveQILjZQM

## POP/IMAP setup
https://support.google.com/mail/answer/7104828?hl=en

https://dev.to/akinwalehabib/how-to-use-spring-integration-with-mail-endpoint-spring-mvc-mongodb-and-react-3890

### How to run
````
java -jar target/email-service-1.0.0.jar --spring.mail.password= --email.password=
````

#### Build
1. Maven Package
   ```shell
   mvn clean package -DskipTests
   ```
2. Docker Build, Push & Run
   ```shell
   docker build -t alokkusingh/email-service:latest -t alokkusingh/email-service:1.0.0 --build-arg JAR_FILE=target/email-service-1.0.0.jar .
   ```
   ```shell
   docker push alokkusingh/email-service:latest
   ```
   ```shell
   docker push alokkusingh/email-service:1.0.0
   ```
   ```shell
   docker run -d -p 8081:8081 --rm --name email-service alokkusingh/email-service --spring.mail.password= --email.password=
   ```