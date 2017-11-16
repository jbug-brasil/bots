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

package br.com.jbugbrasil.bot.service.weather.yahoo;

import br.com.jbugbrasil.bot.service.weather.yahoo.pojo.Item;
import br.com.jbugbrasil.bot.service.weather.yahoo.pojo.YahooQueryResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class YahooWeatherProvider {

    public static final String YAHOO_WEATHER_ENDPOINT = "https://query.yahooapis.com/v1/public/yql";
    public static final String YAHOO_BASE_QUERY = "select item from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")";

    /**
     * Busca a condição climática de determinada cidade
     * @param location Ex: Uberlandia, MG
     * @return retorna a Condição climática da cidade desejada
     */
    public String execute(String location) {

        location = location.replace("\"","");

        String endpointQuery = YAHOO_WEATHER_ENDPOINT + "?q=" + String.format(YAHOO_BASE_QUERY, location) + "&format=json";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(endpointQuery);

        Response response = target.request().get();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Falha ao conectar-se com a API do Yahoo no endereço " + endpointQuery + ", status code is: " + response.getStatus());
        }

        Item condition = response.readEntity(YahooQueryResponse.class).getQuery().getResults().getChannel().getItem();
        StringBuilder builder = new StringBuilder();
        builder.append("<b>" + condition.getTitle() + ":</b>\n");
        builder.append("<pre>" + toCelsius(condition.getCondition().getTemp()) + "°C / ");
        builder.append(condition.getCondition().getTemp() + "°F</pre> - <em>" + condition.getCondition().getText() + "</em>\n");
        builder.append(condition.getLink().split("/\\*")[1]);
        return builder.toString();
    }

    /**
     * Converte temperatura de Fahrenheit para Celsius
     * @param value temperatura em Fahrenheit
     * @return temperatura em Celsius
     */
    private String toCelsius (float value) {
        return String.format("%.1f", (value - 32 ) / 1.8000);
    }

}