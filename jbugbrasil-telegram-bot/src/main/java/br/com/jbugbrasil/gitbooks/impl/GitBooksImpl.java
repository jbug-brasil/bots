package br.com.jbugbrasil.gitbooks.impl;

import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.gitbooks.GitBooks;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class GitBooksImpl implements GitBooks {

    private final Logger log = Logger.getLogger(GitBooksImpl.class.getName());

    @Override
    public String getBooks() {
        StringBuffer responseBuffer = new StringBuffer();
        try {
            HttpResponse response = request();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (inputLine.contains("<a href=\"/book/jboss-books")) {
                    if (inputLine.endsWith("</h4></a>")) {
                        responseBuffer.append(inputLine.substring(inputLine.indexOf("<h4>") + 4, inputLine.indexOf("</h4>")) + "\n");
                    }
                }
            }
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
        return responseBuffer.toString();
    }

    @Override
    public int verifyNewBook() {
        //padrão esperado <a href="/@jboss-books"><i class="octicon octicon-book"></i> 1 Books</a>
        StringBuffer responseBuffer = new StringBuffer();
        try {
            HttpResponse response = request();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (inputLine.contains("<a href=\"/@jboss-books\">")) {
                    responseBuffer.append(inputLine.substring(inputLine.indexOf("</i>") + 4, inputLine.indexOf("Books")).trim());
                }
            }
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
        return Integer.parseInt(responseBuffer.toString());
    }

    @Override
    public String verifyUpdates() {
        return null;
    }

    /*
    * Perform a request againts jboss-books main page
    */
    private HttpResponse request() throws IOException {
        HttpGet request = new HttpGet(BotConfig.GIT_BOOKS_URL);
        request.setHeader(CoreProtocolPNames.HTTP_CONTENT_CHARSET, String.valueOf(Consts.UTF_8));
        return client().execute(request);
    }

    /*
    * Returns the CloseableHttpClient
    */
    private CloseableHttpClient client() {
        RequestConfig config = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();
        return HttpClients.custom().setDefaultRequestConfig(config).build();
    }
}