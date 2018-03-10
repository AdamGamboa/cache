package com.orthanc.commons.cache;

import com.orthanc.commons.cache.event.CacheEntryCreatedListener;
import com.orthanc.commons.cache.event.CacheEntryExpiredListener;
import com.orthanc.commons.cache.event.CacheEntryListener;
import com.orthanc.commons.cache.event.CacheEntryRemovedListener;
import com.orthanc.commons.cache.event.CacheEntryUpdatedListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Adam M. Gamboa G.
 * @param <K> Type of the Key
 * @param <V> Type of the Value
 */
public class CacheConfiguration<K, V> implements Serializable{
    
    private final List<CacheEntryListener <K,V>> listeners;
    private CacheExpiryPolicy cacheExpiryPolicy;
    
    public CacheConfiguration(){
        this.listeners = new ArrayList<>();
    }
    
    public CacheConfiguration<K,V> setExpiryPolicy(CacheExpiryPolicy policy){
        this.cacheExpiryPolicy = policy;
        return this;
    }
    
    protected CacheExpiryPolicy getExpiryPolicy(){
        return this.cacheExpiryPolicy;
    }
    
    public CacheConfiguration<K,V> addCacheEntryListener(CacheEntryListener<K,V> listener){
        this.listeners.add(listener);
        return this;
    }
    
    protected List<CacheEntryListener<K,V>> getCacheEntryListener(){
        return this.listeners;
    }
    
    protected void fireCacheEntryCreatedEvent(K key,CacheEntry<V> entry) {
        this.getCacheEntryListener().stream()
                .filter((l) -> (l instanceof CacheEntryCreatedListener))
                .map((l) -> (CacheEntryCreatedListener<K, V>) l)
                .forEach((createdListener) -> 
                    createdListener.onCreated(key,entry));
    }
    
    protected void fireCacheEntryUpdatedEvent(CacheEntry<V> oldEntry, CacheEntry<V> newEntry) {
        this.getCacheEntryListener().stream()
                .filter((l) -> (l instanceof CacheEntryUpdatedListener))
                .map((l) -> (CacheEntryUpdatedListener<K, V>) l)
                .forEach((updatedListener) -> 
                    updatedListener.onUpdated(oldEntry,newEntry));
    }
    
    protected void fireCacheEntryExpiredEvent(CacheEntry<V> entry) {
        this.getCacheEntryListener().stream()
                .filter((l) -> (l instanceof CacheEntryExpiredListener))
                .map((l) -> (CacheEntryExpiredListener<K, V>) l)
                .forEach((expiredListener) -> 
                    expiredListener.onExpired(entry));
    }

    protected void fireCacheEntryRemovedEvent(K key, CacheEntry<V> entry) {
        this.getCacheEntryListener().stream()
                .filter((l) -> (l instanceof CacheEntryRemovedListener))
                .map((l) -> (CacheEntryRemovedListener<K, V>) l)
                .forEach((removedListener) -> 
                    removedListener.onRemoved(key, entry));
    }
}
