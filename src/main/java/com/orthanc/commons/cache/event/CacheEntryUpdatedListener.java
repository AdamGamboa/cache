package com.orthanc.commons.cache.event;

import com.orthanc.commons.cache.CacheEntry;



/**
 *
 * @author Adam M. Gamboa G
 * @param <K>
 * @param <V>
 */
public interface CacheEntryUpdatedListener <K,V> extends CacheEntryListener<K, V>{
    
    /**
     *
     * @param oldValue
     * @param newValue
     */
    void onUpdated(CacheEntry<V> oldValue, CacheEntry<V> newValue);
}
