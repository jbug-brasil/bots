### Serviço de Cache

Um serviço de cache personalizado utilizado pela api e pelos diversos outros serviços do Bot.
Para o cache é utilizado o Infinispan Embedded que provê tudo que é necessário para ter o cache em execução com o mínimo
possível de configuração.

Este serviço de cache provê alguns caches específicos para alguns serviços, por exemplo o KarmaCache.
Seu uso é bem simples, basta injetar o cache utilizando @Inject e @KarmaCache, por exemplo, que o cache será automaticamente
configurado por este serviço e pronto para uso assim que o Bot entra em execução.

Veja neste exemplo como é fácil utilizar este serviço:

```java

@javax.inject.Inject
@br.com.jbugbrasil.bot.service.cache.qualifier.KarmaCache
private Cache<String, Integer> cache;

public void someMethod() {
    cache.put("key", 10);
}
```

Para utilizar um cache generico, basta injetar o `@DefaultCache`.

### Encontrou bugs ou tem alguma sugestão?
Não hesite em nos procurar, registre um issue ou nos envie um email: contato@jbugbrasil.com.br