# JBug Brasil Telegram bot

O jbug-brasil-bot é o bot utilizado no canal do telegram **JBug Brasil**, para entrar no grupo utilize este link: http://telegram.me/jbug_brasil

##### Para conbtribuir realize fork do projeto e envie suas alterações. :)

### Funções do Bot:
    - karma - Karma operations username++|--
    - /ping - o bot irá responder pong ao usuário que requisitou
    - /books - irá listar todos os livros disponíveis [aqui](https://www.gitbook.com/@jboss-books)
    - /karma - Pesquisa os pontos de karma do username desejado, exemplo: /getkarma spolti
    - /help - Menu de ajuda.
    - /faq - Pesquisa algum projeto previamente cadastrado através do arquivo faq-properties.json
    - /uptime - informa o tempo que o bot está no ar.
    - /urban - pesquisa por um termo em inglês no urban dictionary, exemplo /ub -c 2 -e lol ou /ub lol
    
### Iniciando o Bot

Execute o seguinte comando:

```sh
java -jar -Xms150m -Xmx300m -XX:MetaspaceSize=100m \
    -Dbr.com.jbugbrasil.bot.telegram.token=<TELEGRAM_TOKEN> 
    -Dbr.com.jbugbrasil.bot.telegram.userId=<BOT_USER_ID> \
    -Dbr.com.jbugbrasil.bot.telegram.chatId=<CHAT_ID> \
    -Dbr.com.jbugbrasil.bot.gitbook.token=<GIT_BOOK_TOKEN> telegram-bot-<VERSAO>-swarm.jar
 ```
    
Obs: O token é de uso privado de cada bot, caso deseje utilizar este bot é necessário que registre o mesmo utilizando o BotFather (Bot para registro de bots :D do próprio Telegram) e atualize os seguintes parâmetros no arquivo BotConfig.java:

    - BOT_USER_ID
    - DEFAULT_CHAT_ID (este pode ser obtido através dos logs (level FINE))
    - TELEGRAM_TOKEN: bot token, obtido através do BotFather to telegram na administração dos seus bots.
    - GIT_BOOK_TOKEN: Necessário para utilizar a API REST do GitBook.


### Bugs ou sugestões?
Será um prazer receber correções e sugestões para melhoria do bot.


OBS: Este bot foi feito exclusivamente para o grupo do telegram **JBug Brasil**, portanto muitas de suas funcionalidades são voltadas somente para ele, Mas nada impede que você adicione funcionalidades. :)
Porém a API, plugins e comandos podem ser utilizados por outros Bots.