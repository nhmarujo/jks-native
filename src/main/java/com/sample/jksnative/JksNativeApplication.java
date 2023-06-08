package com.sample.jksnative;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ImportRuntimeHints(JksNativeApplication.NativeConfiguration.class)
public class JksNativeApplication {
    public static void main(String[] args) {
        SpringApplication.run(JksNativeApplication.class, args);
    }

    public static class NativeConfiguration implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources()
                    .registerPattern("application.yml")
                    .registerResource(new ClassPathResource("keystore.jks"));
        }
    }

    @RestController
    public static class DecryptionController {
        @Value("${my-secret}")
        private String mySecret;

        @GetMapping("/decrypted")
        public String decrypted() {
            return mySecret;
        }
    }
}
