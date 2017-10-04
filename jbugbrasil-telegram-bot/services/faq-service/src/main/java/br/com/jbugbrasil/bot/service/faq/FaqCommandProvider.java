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

package br.com.jbugbrasil.bot.service.faq;

import br.com.jbugbrasil.bot.service.cache.qualifier.FaqCache;
import br.com.jbugbrasil.bot.service.faq.pojo.Project;
import br.com.jbugbrasil.bot.api.spi.CommandProvider;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class FaqCommandProvider implements CommandProvider {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    @Any
    FaqService service;

    @Inject
    @FaqCache(classToIndex = "br.com.jbugbrasil.bot.faqservice.service.pojo.Project")
    private Cache<String, Project> cache;

    public void load() {
        log.fine("Carregando comando " + this.name());
        service.populateCache();
    }

    @Override
    public Object execute(Optional<String> key) {
        System.out.println("LALALALALAL " + service.query(key.get()));
        return key.get().length() > 0 ? service.query(key.get()) : "Nenhum parâmetro espeficicado, em caso de dúvidas use " + this.name() + " help.";
    }

    @Override
    public String name() {
        return "/faq";
    }

    @Override
    public String help() {
        StringBuilder strBuilder = new StringBuilder("/faq - ");
        strBuilder.append("Pesquisa projetos open source registrados no bot.\n");
        strBuilder.append("Exemplo: <a href=\"/faq hibernate\">/faq hibernate</a>.");
        // TODO strBuilder.append("Exemplo: /faq <provider_name> hibernate.");
        return strBuilder.toString();
    }

}