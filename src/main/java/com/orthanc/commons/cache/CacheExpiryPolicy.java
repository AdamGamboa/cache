package com.orthanc.commons.cache;

import java.time.Duration;

/**
 *
 * @author Adam M. Gamboa G
 */
public interface CacheExpiryPolicy {
    
    public Duration getAccessPolicy();
    
    public Duration getCreatedPolicy();
    
    public Duration getLastUpdatePolicy();
}
