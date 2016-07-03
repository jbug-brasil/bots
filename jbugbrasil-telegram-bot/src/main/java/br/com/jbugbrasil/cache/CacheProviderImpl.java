package br.com.jbugbrasil.cache;

import br.com.jbugbrasil.cache.listeners.KarmaEventListener;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class CacheProviderImpl {

    private static CacheProviderImpl uniqueInstance;
    private EmbeddedCacheManager manager = new DefaultCacheManager();
    private Cache<Object, Integer> cache;

    public CacheProviderImpl() {}

    public static synchronized CacheProviderImpl getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new CacheProviderImpl();
        return uniqueInstance;
    }

    public Cache getCache() {
        if (cache == null) {
            cache = manager.getCache("wfly-cache");
            cache.addListener(new KarmaEventListener());
        }
        return cache;
    }
}