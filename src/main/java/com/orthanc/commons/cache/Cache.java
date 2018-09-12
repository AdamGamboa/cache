package com.orthanc.commons.cache;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Adam M. Gamboa G
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> implements Serializable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Cache.class);
    
    private Map<K, CacheEntry<V>> data;
    private CacheConfiguration cacheConfiguration;

    /**
     * Constructor
     *
     */    
    public Cache(){
    }

    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>>   Methods  <<>><<>><<>><<>><<>><<>><<>><<>><<>>
    //<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>
    /**
     * Stores a value into the Cache.
     *
     * @param key Key of the value to put in the cache
     * @param value Value to store in the cache
     * @return Stored value
     */
    public V put(K key, V value) {
        CacheEntry<V> entry = this.data.get(key);
        if (entry == null) {
            entry = new CacheEntry<>();
            entry.setEntry(value);
            this.data.put(key, entry);
            this.cacheConfiguration.fireCacheEntryCreatedEvent(key, entry);
        }else{
            CacheEntry<V> oldEntry = entry.copy();
            entry.setEntry(value);
            entry.setLastUpdate();
            this.data.put(key, entry);
            this.cacheConfiguration.fireCacheEntryUpdatedEvent(oldEntry, entry);
        }
        return value;
    }

    /**
     * Gets a value by its key
     *
     * @param key Key
     * @return Value found.
     */
    public V get(K key) {
        CacheEntry<V> entry = this.data.get(key);
        if (entry != null) {
            entry.setLastAccess();
            return entry.getEntry();
        }
        return null;
    }

    /**
     * Removes all the objects in the cache
     */
    public void clear() {
        this.data.clear();
    }

    /**
     * Checks all the entries in the cache to find out and remove the entries
     * that has expired according to the expiry policies added to the Cache.
     */
    public void refresh() {
        CacheExpiryPolicy expiryPolicy = this.cacheConfiguration.getExpiryPolicy();
        if (expiryPolicy != null) {
            //Checks expiry policy for creation
            if (expiryPolicy.getCreatedPolicy() != null) {
                this.data.entrySet()
                        .removeIf(e -> {
                            boolean toBeRemoved = e.getValue() != null
                                    && Duration.between(e.getValue().getCreated(), LocalDateTime.now())
                                    .compareTo(expiryPolicy.getCreatedPolicy()) > 0;
                            if (toBeRemoved) {
                                this.cacheConfiguration.fireCacheEntryExpiredEvent(e.getValue());
                                this.cacheConfiguration.fireCacheEntryRemovedEvent(e.getKey(), e.getValue());
                            }
                            return toBeRemoved;
                        });

            }
            //Checks expiry policy for last access
            if (expiryPolicy.getAccessPolicy() != null) {
                this.data.entrySet()
                        .removeIf(e -> {
                            boolean toBeRemoved = e.getValue() != null 
                                    && ((   e.getValue().getLastAccess() != null
                                            && Duration.between(e.getValue().getLastAccess(), LocalDateTime.now())
                                            .compareTo(expiryPolicy.getAccessPolicy()) > 0)
                                        || (e.getValue().getLastAccess() == null
                                            && Duration.between(e.getValue().getCreated(), LocalDateTime.now())
                                            .compareTo(expiryPolicy.getAccessPolicy()) > 0 ));
                            if (toBeRemoved) {
                                this.cacheConfiguration.fireCacheEntryExpiredEvent(e.getValue());
                                this.cacheConfiguration.fireCacheEntryRemovedEvent(e.getKey(), e.getValue());
                            }
                            return toBeRemoved;
                        });
            }
            //Checks expiry policy for last update
            if (expiryPolicy.getLastUpdatePolicy() != null) {
                this.data.entrySet()
                        .removeIf(e -> {
                            boolean toBeRemoved = e.getValue() != null 
                                    && ((   e.getValue().getLastUpdate() != null
                                            && Duration.between(e.getValue().getLastUpdate(), LocalDateTime.now())
                                            .compareTo(expiryPolicy.getLastUpdatePolicy()) > 0)
                                        ||( e.getValue().getLastUpdate() == null
                                            && Duration.between(e.getValue().getCreated(), LocalDateTime.now())
                                            .compareTo(expiryPolicy.getLastUpdatePolicy()) > 0));
                            if (toBeRemoved) {
                                this.cacheConfiguration.fireCacheEntryExpiredEvent(e.getValue());
                                this.cacheConfiguration.fireCacheEntryRemovedEvent(e.getKey(), e.getValue());
                            }
                            return toBeRemoved;
                        });
            }
        }
    }
    
    public void setConfiguration(CacheConfiguration<K,V> config){
        this.cacheConfiguration = config;
        this.data = this.cacheConfiguration.getCacheStoreInjector().getStore();
    }

    /**
     * Gets the configuration instance of the Cache
     *
     * @return Configuration instance
     */
    protected CacheConfiguration<K, V> getConfiguration() {
        return this.cacheConfiguration;
    }
    
}
