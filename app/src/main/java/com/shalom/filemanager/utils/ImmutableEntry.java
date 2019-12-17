
package com.shalom.filemanager.utils;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * From: https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/ImmutableEntry.java
 * Author: Guava
 */
public class ImmutableEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private final V value;

    public ImmutableEntry(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Nullable
    @Override
    public final K getKey() {
        return key;
    }

    @Nullable
    @Override
    public final V getValue() {
        return value;
    }

    @Override
    public final V setValue(V value) {
        throw new UnsupportedOperationException();
    }

}