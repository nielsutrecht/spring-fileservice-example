package com.nibado.example.fileservice;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    @TestFactory
    List<DynamicTest> sizes() {
        return Stream.of(32, 64, 122).map(i -> DynamicTest.dynamicTest("" + i, () -> {
            var characters = (int)Math.ceil(
                    Math.log(Math.pow(2, i)) / Math.log(64)
            );
            System.out.println(i + " -> " + characters);
        })).toList();
    }
}