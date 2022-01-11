package com.nibado.example.fileservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FSInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(FSInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        File tempDir = null;
        try {
            tempDir = Files.createTempDirectory("files").toFile();
            tempDir.deleteOnExit();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }

        TestPropertyValues
                .of("save-dir:" + tempDir.getAbsolutePath())
                .applyTo(context);

        LOG.info("Set up temp dir for testing: {}", tempDir.getAbsolutePath());
    }
}
