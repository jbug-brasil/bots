package br.com.jbugbrasil.gitbooks.impl;

import br.com.jbugbrasil.Component;
import br.com.jbugbrasil.cache.CacheProviderImpl;
import br.com.jbugbrasil.conf.BotConfig;
import br.com.jbugbrasil.gitbooks.GitBooks;
import br.com.jbugbrasil.gitbooks.pojo.Books;
import br.com.jbugbrasil.gitbooks.pojo.Counts;
import br.com.jbugbrasil.gitbooks.pojo.JSONResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class GitBooksImpl implements GitBooks, Component {

    private final Logger log = Logger.getLogger(GitBooksImpl.class.getName());
    private final CacheProviderImpl cache = CacheProviderImpl.getInstance();

    @Override
    public List<Books> getBooks() throws IOException {
        initialize();
        JSONResponse jsonResponse = (JSONResponse) cache.getCache().get("jsonResponse");
        return jsonResponse.getList();
    }

    @Override
    public int verifyNewBook() {
        initialize();
        JSONResponse jsonResponse = (JSONResponse) cache.getCache().get("jsonResponse");
        return jsonResponse.getTotal();
    }

    @Override
    public String verifyUpdates() {
        return null;
    }

    /*
    * Perform a request againts jboss-books main rest api and put the JSONObject in the cache
    */
    public void initialize() {

        if (!cache.getCache().containsKey("jsonResponse")) {
            log.info("Cache expirou ou está vazio, populoando-o novamente com as informações do gitbooks...");
            HttpGet request = new HttpGet(BotConfig.GIT_BOOKS_ENDPOINT);
            request.setHeader("Authorization", "Bearer " + BotConfig.GIT_BOOKS_USER_TOKEN);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };

            try {
                ObjectMapper mapper = new ObjectMapper();
                JSONResponse jsonResponse = mapper.readValue(client().execute(request, responseHandler), JSONResponse.class);
                cache.getCache().put("jsonResponse", jsonResponse, 30, TimeUnit.MINUTES);
                log.info("Done.........");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
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