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

package br.com.jbugbrasil;

import br.com.jbugbrasil.bot.JBugBrasilBot;
import br.com.jbugbrasil.bot.api.conf.systemproperties.BotProperty;
import br.com.jbugbrasil.bot.api.spi.CommandProvider;
import br.com.jbugbrasil.bot.api.spi.PluginProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Singleton
@javax.ejb.Startup
public class Startup {

    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    @BotProperty(name = "br.com.jbugbrasil.bot.telegram.userId", required = true)
    String userId;

    @Inject
    private JBugBrasilBot bot;

    @Inject
    private Instance<CommandProvider> commands;

    @Inject
    private Instance<PluginProvider> plugins;

    @PostConstruct
    public void startupTasks() {
        try {
            // Carregando comandos
            commands.forEach(command -> command.load());
            // Carregando plugins
            plugins.forEach(plugin -> plugin.load());

            bot.start();
            log.info(userId + " iniciado com sucesso.");
        } catch (final Exception e) {
            e.printStackTrace();
            log.severe("Falha ao registrar o Bot: " + e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        bot.stop();
    }

}