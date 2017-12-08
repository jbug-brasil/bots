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

package br.com.jbugbrasil.bot.plugin.sed;

import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.api.spi.PluginProvider;
import br.com.jbugbrasil.bot.plugin.sed.processor.SedResponse;
import br.com.jbugbrasil.bot.service.cache.qualifier.DefaultCache;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@ApplicationScoped
public class SedPlugin implements PluginProvider {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final String MSG_TEMPLATE = "<i>%s</i> quis dizer <b>%s</b>";

    @Inject
    @DefaultCache
    Cache<Long, String> cache;

    @Override
    public String process(MessageUpdate update) {

        if (update.getMessage().getText().startsWith("/")) {
            log.fine("Sed plugin - Ignoring igonrando comando [" + update.getMessage().getText() + "]");
            return null;
        }
        SedResponse sedResponse = new SedResponse().process(update);
        log.fine("Sed Plugin - " + sedResponse.toString());
        if (sedResponse.isProcessable() && cache.containsKey(sedResponse.getUser_id())) {
            if (cache.get(sedResponse.getUser_id()).contains(sedResponse.getOldString())) {
                String newValue = null;
                if (sedResponse.isFullReplace()) {
                    newValue = cache.get(sedResponse.getUser_id()).replace(sedResponse.getOldString(), sedResponse.getNewString());
                } else {
                    newValue = cache.get(sedResponse.getUser_id()).replaceFirst(sedResponse.getOldString(), sedResponse.getNewString());
                }
                cache.replace(sedResponse.getUser_id(), newValue);
                return String.format(MSG_TEMPLATE, sedResponse.getUsername(), newValue);
            }
        } else if (!sedResponse.isProcessable() && !update.getMessage().getText().startsWith("s/")) {
            if (cache.containsKey(sedResponse.getUser_id())) {
                cache.replace(sedResponse.getUser_id(), update.getMessage().getText(), 60, TimeUnit.MINUTES);
            } else {
                cache.put(sedResponse.getUser_id(), update.getMessage().getText(), 60, TimeUnit.MINUTES);
            }
        }
        return null;
    }

    @Override
    public void load() {
        log.fine("Carregando plugin sed");
    }

}