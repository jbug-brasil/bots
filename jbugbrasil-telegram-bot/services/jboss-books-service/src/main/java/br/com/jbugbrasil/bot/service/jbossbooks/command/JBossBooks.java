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

package br.com.jbugbrasil.bot.service.jbossbooks.command;

import br.com.jbugbrasil.bot.api.object.MessageUpdate;
import br.com.jbugbrasil.bot.service.jbossbooks.JBossBooksService;
import br.com.jbugbrasil.bot.api.spi.CommandProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class JBossBooks implements CommandProvider {

    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private JBossBooksService service;

    @Override
    public void load() {
        log.fine("Carregando comando " + this.name());
        service.initialize();
    }

    @Override
    public Object execute(Optional<String> key, MessageUpdate messageUpdate) {
        StringBuilder response = new StringBuilder();
        try {
            service.getBooks().stream()
                    .filter(book -> book.isPublic())
                    .forEach(b -> {
                        response.append("<pre>" + b.getTitle() + "</pre>");
                        response.append(" - ");
                        response.append("<a href=\"" +  b.getUrls().getRead() + "\">Ler</a> / ");
                        response.append("<a href=\"" + b.getUrls().getDownload().getPdf() + "\">Download</a>");
                        response.append("\n");
                    });
        } catch (final Exception e) {
            log.warning("Falha ao executar comando [" + this.name() + "]: " + e.getMessage());
        }
        return response.toString();
    }

    @Override
    public String name() {
        return "/books";
    }

    @Override
    public String help() {
        return this.name() + " - Lista os livros disponíveis em https://www.gitbook.com/@jboss-book";
    }
}
