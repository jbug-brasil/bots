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

package br.com.jbugbrasil.bot.service.persistence.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AMOUNTOFBOOKS")
public class AmountOfBooks {

    /**
     * Executar no banco de prod
     * <p>
     * alter table AMOUNTOFBOOKS alter column AMOUNT set not null;
     * alter table AMOUNTOFBOOKS add id integer(1) default '1' not null;
     * alter table AMOUNTOFBOOKS add primary key (id);
     * insert into AMOUNTOFBOOKS (ID,AMOUNT) values (0,0);
     */

    @Id
    private int id;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    public AmountOfBooks(int amount) {
        this.amount = amount;
    }

    public AmountOfBooks() {
    }

    public int getId() {
        return 0;
    }

    public void setId(int id) {
        this.id = 0;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AmountOfBooks{" +
                "amount=" + amount +
                '}';
    }
}
