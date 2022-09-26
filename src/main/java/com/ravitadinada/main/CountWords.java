package com.ravitadinada.main;

import com.ravitadinada.collection.TestHashMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CountWords {

    public static void main(String[] args) throws IOException {
        String inputFileName = args[0];

        List<String> lines = Files.readAllLines(Paths.get(inputFileName));

        List<String> words = lines.stream()
                .flatMap(line -> Arrays.stream(line.split("\\W+")))
                .toList();

        TestHashMap<String, Integer> counts = new TestHashMap<>();
        for (String word : words) {
            int count = counts.get(word).orElse(0);
            counts.put(word, count + 1);
        }

        System.out.printf("Num words: %d%n", counts.size());
        for (String word : counts.getKeys()) {
            System.out.printf("%s: %d%n", word, counts.get(word).orElse(0));
        }
    }

}
