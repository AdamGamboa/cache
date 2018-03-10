package com.orthanc.commons.cache;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Adam M. Gamboa G.
 * @param <V>
 */
public class CacheEntry<V> implements Serializable{
    
    private LocalDateTime created;
    private LocalDateTime lastAccess;
    private LocalDateTime lastUpdate;
    private V entry;
    
    public CacheEntry(){
        this.created = LocalDateTime.now();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public V getEntry() {
        return entry;
    }

    public void setEntry(V entry) {
        this.entry = entry;
    }
    
    protected void setLastUpdate(){
        this.lastUpdate = LocalDateTime.now();
    }
    
    protected void setLastAccess(){
        this.lastAccess = LocalDateTime.now();
    }
    
    protected CacheEntry<V> copy(){
        CacheEntry clone = new CacheEntry();
        clone.created = this.created;
        clone.lastAccess = this.lastAccess;
        clone.lastUpdate = this.lastUpdate;
        clone.entry = this.entry;
        return clone;
    }

}
