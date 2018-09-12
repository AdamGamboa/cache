package com.orthanc.commons.cache.di;

import com.orthanc.commons.cache.CacheEntry;
import java.util.Map;

/**
 *
 * @author Adam M. Gamboa
 * @param <K>
 * @param <V>
 */
public interface CacheStoreInjector<K, V>{
    
    Map<K, CacheEntry<V>> getStore();
}
