package com.orthanc.commons.cache.event;

import com.orthanc.commons.cache.CacheEntry;

/**
 *
 * @author Adam M. Gamboa G
 * @param <K>
 * @param <V>
 */
public interface CacheEntryCreatedListener <K,V> extends CacheEntryListener<K, V> {
 
    void onCreated(K key, CacheEntry<V> createdValue);
}
