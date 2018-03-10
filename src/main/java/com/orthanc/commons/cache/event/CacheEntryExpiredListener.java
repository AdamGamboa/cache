package com.orthanc.commons.cache.event;

import com.orthanc.commons.cache.CacheEntry;


/**
 *
 * @author Adam. M. Gamboa G
 * @param <K>
 * @param <V>
 */
public interface CacheEntryExpiredListener <K,V> extends CacheEntryListener<K, V> {
    
    void onExpired(CacheEntry<V> expiredValue);
}
