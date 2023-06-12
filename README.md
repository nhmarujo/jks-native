# jks-native

This is a simple application to reproduce an issue loading, in a `native image`, a `jks` file to use with [Spring Cloud Decryption](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_encryption_and_decryption).

# Running with JVM works

To test it, you can build the JAR and run it:
```bash
# Build JAR
./mvnw clean package

# Run application
java -jar target/jks-native-0.0.1-SNAPSHOT.jar
```

And then get the decrypted secret (`my-secret` in `application.yml`):
```bash
# Get decrypted secret
curl http://localhost:8080/decrypted
```

You will get as output:
```
my awesome secret
```

# Running as native image doesn't work

To reproduce it, you can build the native image and try to run it:
```bash
# Build executable
./mvnw clean native:compile -Pnative

# Run application
./target/jks-native
```

The application will fail to run with the following error:
```java
12:15:38.869 [main] ERROR org.springframework.boot.SpringApplication -- Application run failed
java.lang.NullPointerException: null
        at org.springframework.cloud.context.encrypt.EncryptorFactory.create(EncryptorFactory.java:55)
        at org.springframework.cloud.bootstrap.encrypt.TextEncryptorUtils.getTextEncryptor(TextEncryptorUtils.java:66)
        at org.springframework.cloud.bootstrap.encrypt.TextEncryptorUtils.decrypt(TextEncryptorUtils.java:51)
        at org.springframework.cloud.bootstrap.encrypt.DecryptEnvironmentPostProcessor.postProcessEnvironment(DecryptEnvironmentPostProcessor.java:66)
        at org.springframework.boot.env.EnvironmentPostProcessorApplicationListener.onApplicationEnvironmentPreparedEvent(EnvironmentPostProcessorApplicationListener.java:109)
        at org.springframework.boot.env.EnvironmentPostProcessorApplicationListener.onApplicationEvent(EnvironmentPostProcessorApplicationListener.java:94)
        at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172)
        at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165)
        at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:143)
        at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:131)
        at org.springframework.boot.context.event.EventPublishingRunListener.multicastInitialEvent(EventPublishingRunListener.java:136)
        at org.springframework.boot.context.event.EventPublishingRunListener.environmentPrepared(EventPublishingRunListener.java:81)
        at org.springframework.boot.SpringApplicationRunListeners.lambda$environmentPrepared$2(SpringApplicationRunListeners.java:64)
        at java.base@17.0.6/java.lang.Iterable.forEach(Iterable.java:75)
        at org.springframework.boot.SpringApplicationRunListeners.doWithListeners(SpringApplicationRunListeners.java:118)
        at org.springframework.boot.SpringApplicationRunListeners.doWithListeners(SpringApplicationRunListeners.java:112)
        at org.springframework.boot.SpringApplicationRunListeners.environmentPrepared(SpringApplicationRunListeners.java:63)
        at org.springframework.boot.SpringApplication.prepareEnvironment(SpringApplication.java:355)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:306)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1305)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1294)
        at com.sample.jksnative.JksNativeApplication.main(JksNativeApplication.java:17)
```

### Some notes

- Although not sure if it is needed, I'm adding both `application.yml` and `keystore.jks` on the native resource hints, to be sure they are considered at build time.
- It is obviously a bad practice to have the keystore and the details to unlock in the same place. This is simply for test purpose. On a normal application the details to unlock the keystore would get injected on boot.