package com.ravitadinada.collection;

import java.util.*;
import java.util.stream.Collectors;

public class TestHashMap<K, V> {

    private final static int INIT_CAPACITY = 10;
    private final static double FILL_CAPACITY = 0.5;

    private List<List<Entry<K,V>>> buckets;
    private int size;

    public TestHashMap() {
        this.buckets = createEmptyBuckets(INIT_CAPACITY);
        this.size = 0;
    }

    public Optional<V> get(K key) {
        if (key == null) {
            return Optional.empty();
        }

        List<Entry<K, V>> bucket = getBucket(key, buckets);
        Optional<Entry<K, V>> maybeEntry = getEntryFromBucket(key, bucket);

        return maybeEntry.map((entry) -> entry.value);
    }

    public Optional<V> put(K key, V value) {
        if ((double) this.size() / buckets.size() > FILL_CAPACITY) {
            doubleBucketsSize();
        }
        Optional<V> prevVal = this.putIntoBuckets(key, value, buckets);
        if (prevVal.isEmpty()) {
            this.size += 1;
        }
        return prevVal;
    }

    public Optional<V> remove(K key) {
        if (key == null) {
            return Optional.empty();
        }

        List<Entry<K, V>> bucket = getBucket(key, buckets);
        Optional<Entry<K, V>> maybeEntry = getEntryFromBucket(key, bucket);

        if (maybeEntry.isPresent()) {
            Entry<K, V> entry = maybeEntry.get();
            bucket.remove(entry);
            this.size -= 1;
            return Optional.of(entry.value);
        }
        return Optional.empty();
    }

    public int size() {
        return size;
    }

    public Set<K> getKeys() {
        return this.getAllEntries().stream()
                .map((entry) -> entry.key)
                .collect(Collectors.toSet());
    }

    public List<V> getValues() {
        return this.getAllEntries().stream()
                .map((entry) -> entry.value)
                .collect(Collectors.toList());
    }

    private List<List<Entry<K,V>>> createEmptyBuckets(int capacity) {
        List<List<Entry<K,V>>> buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(new LinkedList<>());
        }
        return buckets;
    }

    private void doubleBucketsSize() {
        List<List<Entry<K,V>>> newBuckets = createEmptyBuckets(buckets.size() * 2);
        for (Entry<K, V> entry : this.getAllEntries()) {
            putIntoBuckets(entry.key, entry.value, newBuckets);
        }
        buckets = newBuckets;
    }

    private List<Entry<K, V>> getAllEntries() {
        return buckets.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Optional<V> putIntoBuckets(K key, V value, List<List<Entry<K,V>>> buckets) {
        List<Entry<K, V>> bucket = getBucket(key, buckets);
        Optional<Entry<K, V>> maybeEntry = getEntryFromBucket(key, bucket);

        if (maybeEntry.isPresent()) {
            Entry<K, V> entry = maybeEntry.get();
            V prevVal = entry.value;
            entry.value = value;
            return Optional.of(prevVal);
        } else {
            bucket.add(new Entry<>(key, value));
            return Optional.empty();
        }
    }

    private List<Entry<K, V>> getBucket(K key, List<List<Entry<K,V>>> buckets) {
        if (key == null) {
            throw new IllegalArgumentException("Null values cannot be used as keys");
        }

        int hashCode = key.hashCode();
        int bucketIndex = hashCode % buckets.size();
        return buckets.get(bucketIndex);
    }

    private Optional<Entry<K, V>> getEntryFromBucket(K key, List<Entry<K, V>> bucket) {
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
