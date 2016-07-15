package br.com.jbugbrasil.gitbooks.impl;

import br.com.jbugbrasil.gitbooks.GitBooks;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
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

    private final String USER_AGENT = "Mozilla/5.0";
    private final String GIT_BOOKS_URL = "https://www.gitbook.com/@jboss-books";

    private static BasicCookieStore cookieStore = new BasicCookieStore();

    @Override
    public String getBooks() {
        HttpGet request = new HttpGet(GIT_BOOKS_URL);
        StringBuffer responseBuffer = new StringBuffer();
        request.setHeader(CoreProtocolPNames.HTTP_CONTENT_CHARSET, String.valueOf(Consts.UTF_8));

        try {
            HttpResponse response = client().execute(request);
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
    public boolean verifyNewBook() {
        return false;
    }

    @Override
    public String verifyUpdates() {
        return null;
    }


    /*
    * Returns the CloseableHttpClient
    */
    private CloseableHttpClient client() {

        return HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setUserAgent(USER_AGENT)
                .build();

    }

}