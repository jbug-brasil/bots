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

package br.com.jbugbrasil.bot.service.urbandictionary;

import br.com.jbugbrasil.bot.service.urbandictionary.helper.Helper;
import br.com.jbugbrasil.bot.api.spi.CommandProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class UrbanDictionary implements CommandProvider {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    @Any
    private Helper helper;


    @Override
    public void load() {
        log.fine("Carregando comando " + this.name());
    }

    @Override
    public Object execute(Optional<String> key) {
        return key.get().length() > 0 ? helper.query(key.get().toString()) : "Nenhum parâmetro encontrado, em caso de dúvidas utilize " + this.name() + " help.";
    }

    @Override
    public String name() {
        return "/urban";
    }

    @Override
    public String help() {
        return this.name() + " - Pesquisa por um termo/gíria em inglês.\n " +
                "<b>-c N</b> seta o número de resultados.\n" +
                "<b>-e</b> adiciona um exemplo de como usar a gíria.\n" +
                "<b>Exemplo:</b> /define -c 2 -e lol";
    }
}
