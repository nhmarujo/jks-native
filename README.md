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
```
Description:

Failed to bind properties under 'encrypt.key-store.location' to org.springframework.core.io.Resource:

    Property: encrypt.key-store.location
    Value: "classpath:/keystore.jks"
    Origin: class path resource [application.yml] - 3:13
    Reason: org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [org.springframework.core.io.Resource]

Action:

Update your application's configuration
```

### Some notes

- Although not sure if it is needed, I'm adding both `application.yml` and `keystore.jks` on the native resource hints, to be sure they are considered at build time.
- It is obviously a bad practice to have the keystore and the details to unlock in the same place. This is simply for test purpose. On a normal application the details to unlock the keystore would get injected on boot.