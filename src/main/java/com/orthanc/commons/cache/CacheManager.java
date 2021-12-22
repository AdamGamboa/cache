package com.orthanc.commons.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Adam M. Gamboa G.
 */
public class CacheManager implements Serializable{
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
    public static final String DEFAULT_CACHE_NAME = "Default";
    private static CacheManager INSTANCE;
    
    private final Map<String,Cache> caches = new HashMap<>();
    private Timer timer;
    
    /**
     * Constructor
     */
    private CacheManager(){
        this.initializeCacheManager();
    }
    
    /**
     * Gets the singleton instance
     * @return Instance
     */
    public static CacheManager getInstance(){
        if(INSTANCE == null){
            initializeSingleton();
        }
        return INSTANCE;
    }
    
    /**
     * Initializes the Singleton
     */
    private static void initializeSingleton(){
        INSTANCE = new CacheManager();
    }
    
    /**
     * Initializes the Cache Manager
     */
    private void initializeCacheManager(){
        try{
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    refresh();
                }
            };
            this.timer = new Timer("Cache Manager Timer");
            int interval = 30*1000; 
            timer.scheduleAtFixedRate(timerTask, 1000, interval);
        }catch(Exception ex){
            LOGGER.error("Imposible to initialize the timer 'Cache Manager Timer'",ex);
        }
    }
    
    /**
     * Creates a new Cache.
     * 
     * @param <K> Type of the key of the cache
     * @param <V> Type of the value in the cache
     * @param cacheId Identity of the cache
     * @param configuration Configuration for the created cache
     * @return  The instance of the cache created.
     */
    public <K, V> Cache<K, V> createCache(String cacheId, CacheConfiguration<K,V> configuration){
        if(cacheId == null || cacheId.trim().isEmpty()){
            cacheId = DEFAULT_CACHE_NAME;
        }
        
        if(caches.containsKey(cacheId)){
           throw new RuntimeException("Already exists a Cache Id:"+cacheId); 
        }else{
            Cache<K,V> newCache = new Cache<>();
            newCache.setConfiguration(configuration);
            caches.put(cacheId, newCache);
            return newCache;
        }
    }
    
    /**
     * Gets a cache by its identifier. 
     * @param <K> Key type
     * @param <V> Value type
     * @param cacheId Identifier of the cache to retrieve.
     * @param keyType Class of the expected Key type
     * @param valueType Class of the expected Value type
     * @return Cache instance found
     */
    public  <K, V> Cache<K, V>  getCache(String cacheId, Class<K> keyType, Class<V> valueType){
        Cache cache = this.caches.get(cacheId);
        if(cache != null){
            return  cache;
        }
        return null;
    }
    
    /**
     * Method that is executed every 30seconds to refresh the content of the caches
     * that are handled by this CacheManager Instance.
     **/
    public void refresh(){
        try{
            LOGGER.debug("Executing refresh Cache Manager");
            caches.forEach((key,cache) -> {
                LOGGER.debug("Starting refresh Cache:"+key);
                cache.refresh();
                LOGGER.debug("Finishing refresh Cache:"+key);
            });
        }catch(Exception ex){
            LOGGER.error("ERROR refreshing the Cache Manager.",ex);
        }
    }
    
    /**
     * Envia a cancelar las ejecuciones del time cuando se va a destruir el 
     * singleton
     */
    @PreDestroy
    public void onDestroy(){
        try{
            this.timer.cancel();
            this.timer = null;
        }catch(Exception ex){
            LOGGER.error("Error deteniendo el Cache Manager Timer");
        }
    }
    
    @Override
    public void finalize() throws Throwable{
        if(this.timer != null){
            onDestroy();
        }
        super.finalize();
    }
    
    
}
