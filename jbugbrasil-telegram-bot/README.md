# JBug Brasil Telegram bot

O jbug-brasil-bot é o bot utilizado no canal do telegram **JBug Brasil**, para entrar no grupo utilize este link: http://telegram.me/jbug_brasil

##### Para conbtribuir realize fork do projeto e envie suas alterações. :)

### Funções do Bot:
    - karma - Karma operations username++|--
    - ping - o bot irá responder pong ao usuário que requisitou
    - /getbooks - irá listar todos os livros disponíveis [aqui](https://www.gitbook.com/@jboss-books)
    - /getkarma - Pesquisa os pontos de karma do username desejado, exemplo: /getkarma spolti
    - /help - Menu de ajuda.
    - /faq - Pesquisa algum projeto previamente cadastrado através do arquivo faq-properties.json
    - /uptime - informa o tempo que o bot está no ar.
    - /ub - pesquisa por um termo em inglês no urban dictionary, exemplo /ub -c 2 -e lol ou /ub lol
    
### Iniciando o Bot

Execute o seguinte comando:

```sh
java -jar  -Dbr.com.jbugbrasil.telegram.token=<TOKEN> -Dbr.com.jbugbrasil.telegram.userId=<JBUG_BRASIL_BOT_USER> -Dbr.com.jbugbrasil.telegram.chatId=<DEFAULT_CHAT_ID> -Dbr.com.jbugbrasil.gitbooks.token=<GIT_BOOKS_TOKEN> telegram.bot-2.0.Final-swarm.jar

Obs: O token é de uso privado de cada bot, caso deseje utilizar este bot é necessário que registre o mesmo utilizando o BotFather (Bot para registro de bots :D do próprio Telegram) e atualize os seguintes parâmetros no arquivo BotConfig.java:

    - JBUG_BRASIL_BOT_USER
    - DEFAULT_CHAT_ID (este pode ser obtido através dos logs (level FINE)
    - TOKEN: bot token, obtido através do BotFather to telegram na administração dos seus bots.
    - GIT_BOOKS_TOKEN: Necessário para utilizar a API REST do Gitbooks.

```
Se ocorrer tudo bem na inicialização do bot a seguintes mensagens serão exibidas:

```sh
2016-07-28 00:40:55.223 INFO    (br.com.jbugbrasil.commands.faq.FaqPropertiesLoader <clinit>) Tentando ler o arquivo /META-INF/faq-properties.json 
2016-07-28 00:40:57.249 INFO    (br.com.jbugbrasil.commands.faq.FaqPropertiesLoader load) Cache populado com sucesso. 
2016-07-28 00:40:57.278 INFO    (br.com.jbugbrasil.database.StartH2 startDatabase) Banco de dados iniciado com sucesso; 
[main] INFO org.quartz.impl.StdSchedulerFactory - Using default implementation for ThreadExecutor
[main] INFO org.quartz.simpl.SimpleThreadPool - Job execution threads will use class loader of thread: main
[main] INFO org.quartz.core.SchedulerSignalerImpl - Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl
[main] INFO org.quartz.core.QuartzScheduler - Quartz Scheduler v.2.2.1 created.
[main] INFO org.quartz.simpl.RAMJobStore - RAMJobStore initialized.
[main] INFO org.quartz.core.QuartzScheduler - Scheduler meta-data: Quartz Scheduler (v2.2.1) 'DefaultQuartzScheduler' with instanceId 'NON_CLUSTERED'
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
  Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.

[main] INFO org.quartz.impl.StdSchedulerFactory - Quartz scheduler 'DefaultQuartzScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
[main] INFO org.quartz.impl.StdSchedulerFactory - Quartz scheduler version: 2.2.1
[main] INFO org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
2016-07-28 00:40:57.385 INFO    (br.com.jbugbrasil.Main main) Schedulers iniciados com sucesso. 
2016-07-28 00:40:58.997 INFO    (br.com.jbugbrasil.Main main) jbugbrasil_bot iniciado com sucesso. 
```

### Bugs?
Será um prazer receber correções e sugestões para melhoria do bot.


OBS: Este bot foi feito exclusivamente para o grupo do telegram **JBug Brasil**, portanto muitas de suas funcionalidades são voltadas somente para ele, Mas nada impede que você adicione funcionalidades. :)