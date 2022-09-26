package com.ravitadinada.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

public class TestHashMapTest {

    @Test
    void testGettingKeys() {
        String testKey1 = "abc";
        String testKey2 = "def";

        String testValue1 = "123";

        TestHashMap<String, String> map = new TestHashMap<>();
        map.put(testKey1, testValue1);

        Assertions.assertEquals(1, map.size());

        Optional<String> key1Result = map.get(testKey1);
        Assertions.assertTrue(key1Result.isPresent());
        Assertions.assertEquals(testValue1, key1Result.get());

        Optional<String> key2Result = map.get(testKey2);
        Assertions.assertFalse(key2Result.isPresent());
    }

    static Stream<Arguments> testRemoveProvider() {
        return Stream.of(
                Arguments.of("hello", "world"),
                Arguments.of("abd", "def")
        );
    }

    @ParameterizedTest
    @MethodSource("testRemoveProvider")
    void testRemove(String key, String value) {
        TestHashMap<String, String> map = new TestHashMap<>();

        map.put(key, value);

        Assertions.assertEquals(1, map.size());

        Optional<String> getResult = map.get(key);
        Assertions.assertTrue(getResult.isPresent());
        Assertions.assertEquals(value, getResult.get());

        map.remove(key);

        Assertions.assertEquals(0, map.size());

        Optional<String> getResultAfterRemove = map.get(key);
        Assertions.assertFalse(getResultAfterRemove.isPresent());
    }
}
