/*
 The MIT License (MIT)

 Copyright (c) 2017 JBug:Brasil <contato@jbugbrasil.com.br>

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package br.com.jbugbrasil.bot.plugin.karma.listener;

import br.com.jbugbrasil.bot.service.persistence.pojo.Karma;
import br.com.jbugbrasil.bot.service.persistence.repository.KarmaRepository;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryExpired;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryExpiredEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Listener()
@ApplicationScoped
public class KarmaEventListener {

    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private KarmaRepository karma;

    /**
     * Cada item adicionado no {@link br.com.jbugbrasil.bot.service.cache.qualifier.KarmaCache} irá disparar um evento que irá adicionar/atualizar os pontos de karma da chave
     * na camada de {@link KarmaRepository}
     * @param event {@link CacheEntryCreatedEvent}
     */
    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent event) {
        if (event.getValue() != null) {
            try {
                karma.updateOrCreateKarma(new Karma(event.getKey().toString(), (int) event.getValue()));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Atualiza o karma da chave desejada no banco de dados, qualquer evento que atualiza um valor no
     * {@link br.com.jbugbrasil.bot.service.cache.qualifier.KarmaCache} irá disparar este evento
     * @param event {@link CacheEntryModifiedEvent}
     */
    @CacheEntryModified
    public void entryModified(CacheEntryModifiedEvent event) {
        try {
            karma.updateOrCreateKarma(new Karma(event.getKey().toString(), (int) event.getValue()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Para cada entrada do cache expirada, que teve seu tempo de vida atingido irá disparar este evento e uma mensagem
     * será impressa nos logs.
     * @param event {@link CacheEntryExpiredEvent}
     */
    @CacheEntryExpired
    public void cacheEntryExpired(CacheEntryExpiredEvent event) {
        log.fine("CacheEntryExpired " + event.getKey() + " " + event.getValue().toString());
    }

    /**
     * Qualquer entrada removida do cache irá acionar este evento, sua função é apenas imprimir nos logs informações
     * sobre qual entrada foi removida do {@link br.com.jbugbrasil.bot.service.cache.qualifier.KarmaCache}
     * @param event {@link CacheEntryRemovedEvent}
     */
    @CacheEntryRemoved
    public void entryRemoved(CacheEntryRemovedEvent event) {
        log.info("entry " + event.getKey() + " removed from the cache");
    }

}
