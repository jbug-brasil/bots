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

package br.com.jbugbrasil.bot.service.packt;

import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.api.spi.CommandProvider;
import br.com.jbugbrasil.bot.api.spi.PluginProvider;
import br.com.jbugbrasil.bot.service.packt.notifier.PacktNotifier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class Packt implements CommandProvider {

    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    PacktNotifier packtNotifier;

    @Override
    public void load() {
        log.fine("Carregando comando " + this.name());
        packtNotifier.populate(true);
    }

    @Override
    public Object execute(Optional<String> key, MessageUpdate messageUpdate) {
        if (key.isPresent() && key.get().equals("notify")) return packtNotifier.registerNotification(messageUpdate);
        if (key.isPresent() && key.get().equals("off")) return packtNotifier.unregisterNotification(messageUpdate);
        return packtNotifier.get();
    }

    @Override
    public String name() {
        return "/packt";
    }

    @Override
    public String help() {
        StringBuilder builder = new StringBuilder(this.name() + " - ");
        builder.append("Retorna informações do livro gratuito do dia ofericido pela Packt Publishing\n");
        builder.append("    <code>" + this.name() + "</code> - retorna informações do livro.\n");
        builder.append("    <code>" + this.name() + " notify</code> - Ativa notificação para um grupo ou para um chat privado. Notificações são enviadas diariamente as 23h00.\n");
        builder.append("    <code>" + this.name() + " off</code> - Desabilita as notificações");
        return builder.toString();
    }
}
