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

package br.com.jbugbrasil.bot.service.persistence.repository;


import br.com.jbugbrasil.bot.service.persistence.pojo.Karma;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Transactional
@ApplicationScoped
public class KarmaRepository {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext(unitName = "jbugBrasilBotPU")
    EntityManager em;

    public int get(String key) {
        try {
            return (int) em.createNativeQuery("SELECT points FROM KARMA where username='" + key + "'").getSingleResult();
        } catch (final Exception e) {
            log.fine("Não foi encontrado pontos de karma para a chave [" + key + "]");
            return 0;
        }
    }

    public void updateOrCreateKarma(Karma karma) {
        log.fine("Atualizando karma [" + karma.toString() + "]");
        try {
            em.merge(karma);
            em.flush();
        } catch (final Exception e) {
            log.warning("Falha ao persistir objeto [" + karma.toString() + "]: " + e.getMessage());
        }

    }

}
