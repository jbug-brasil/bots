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

import br.com.jbugbrasil.bot.service.persistence.pojo.AmountOfBooks;
import br.com.jbugbrasil.bot.service.persistence.pojo.BookUpdates;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Transactional
@ApplicationScoped
public class JBossBooksRepository {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext(unitName = "jbugBrasilBotPU")
    EntityManager em;


    public void bookUpdate(BookUpdates update) {
        try {
            log.fine("Salvando quantidade de updates do livro [" + update.getBookName() + " - " + update.getUpdates() + "]");
            em.merge(update);
            em.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void persistAmountOfBooks(AmountOfBooks amountOfBooks) {
        try {
            log.fine("Salvando quantidade de livros: [" + amountOfBooks.getAmount() + "]");
            em.merge(amountOfBooks);
            em.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public int getAmount() {
        Query q = em.createNativeQuery("SELECT AMOUNT from AMOUNTOFBOOKS;");
        return (int) q.getSingleResult();
    }

    public int getBookUpdates(String bookName) {
        try {
            Query q = em.createNativeQuery("SELECT updates FROM BOOKUPDATES where bookName='" + bookName + "'");
            return (int) q.getSingleResult();
        } catch (final Exception e) {
            return 0;
        }
    }

}
