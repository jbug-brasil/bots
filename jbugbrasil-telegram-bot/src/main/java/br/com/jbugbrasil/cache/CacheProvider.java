package br.com.jbugbrasil.cache;

import org.infinispan.Cache;

/**
 * Created by fspolti on 7/2/16.
 */
public interface CacheProvider {

    Cache getCache();

}
