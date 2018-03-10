package com.orthanc.commons.cache.event;

import com.orthanc.commons.cache.CacheEntry;


/**
 *
 * @author Adam M. Gamboa G
 * @param <K>
 * @param <V>
 */
public interface CacheEntryRemovedListener <K,V> extends CacheEntryListener<K, V>{
    
    void onRemoved(K removedKey, CacheEntry<V> removedValue);
}
