package com.orthanc.commons.cache.di;

import com.orthanc.commons.cache.CacheEntry;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adam M. Gamboa G.
 * @param <K>
 * @param <V>
 */
public class DefaultCacheStoreInjector<K,V> implements CacheStoreInjector{

    @Override
    public Map<K, CacheEntry<V>> getStore() {
        return new HashMap<>();
    }
    
}
