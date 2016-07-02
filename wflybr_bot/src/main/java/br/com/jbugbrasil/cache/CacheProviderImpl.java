package br.com.jbugbrasil.cache;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Created by fspolti on 7/2/16.
 */
public class CacheProviderImpl implements CacheProvider {

    private static CacheProviderImpl uniqueInstance;
    private EmbeddedCacheManager manager = new DefaultCacheManager();
    private Cache<Object, Object> cache = manager.getCache("wfly-cache");

    public CacheProviderImpl() {}

    public static synchronized CacheProviderImpl getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new CacheProviderImpl();
        return uniqueInstance;
    }

    @Override
    public Cache getCache() {
        return cache;
    }
}