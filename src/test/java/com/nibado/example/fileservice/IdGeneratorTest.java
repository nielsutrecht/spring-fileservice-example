package com.nibado.example.fileservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void generate() {
        for(var i = 0;i < 1000;i++) {
            assertThat(IdGenerator.generate()).matches("[0-9a-zA-Z-_]{21}");
        }
    }

    @Test
    void testGenerate() {
        assertThat(IdGenerator.generate(11)).matches("[0-9a-zA-Z-_]{11}");
    }
}