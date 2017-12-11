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

import br.com.jbugbrasil.bot.service.persistence.pojo.PacktNotification;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

@Transactional
@ApplicationScoped
public class PacktRepository {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext(unitName = "jbugBrasilBotPU")
    EntityManager em;

    public String register(PacktNotification packtNotification) {
        try {
            em.persist(packtNotification);
            em.flush();
            return "Notificação para <b>" + packtNotification.getChannel() + "</b> registrada.";
        } catch (final Exception e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                if (t.getClass().toString().contains("ConstraintViolationException")) {
                    return "Notificação para <b>" + packtNotification.getChannel() + "</b> já existe.";
                }
            }
            return "Falha ao registrar notificação: " + e.getCause();
        }
    }

    public String unregister(PacktNotification packtNotification) {
        try {
            System.out.println(packtNotification.toString());
            em.remove(em.merge(packtNotification));
            em.flush();
            return "Notificação para <b>" + packtNotification.getChannel() + "</b> removida.";
        } catch (final Exception e) {
            log.warning("Falha ao remover notificação: " + e.getMessage());
            return "Falha ao remover notificação";
        }
    }

    public List<BigInteger> get() {
        try {
            return em.createNativeQuery("SELECT chatId FROM PACKT_NOTIFIER").getResultList();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}