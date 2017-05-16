package br.com.jbugbrasil.cache;

import br.com.jbugbrasil.cache.listeners.KarmaEventListener;
import br.com.jbugbrasil.commands.faq.Project;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class CacheProviderImpl {

    private static CacheProviderImpl uniqueInstance;
    //Allow indexing and queries
    private EmbeddedCacheManager manager = new DefaultCacheManager(new ConfigurationBuilder()
            .indexing()
                .autoConfig(true)
                .addIndexedEntity(Project.class)
                .index(Index.ALL)
            .addProperty("default.directory_provider", "ram")
            .build());
    private Cache<Object, Integer> cache;

    public CacheProviderImpl() {
    }

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