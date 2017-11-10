### JBug:Brasil Telegram API

API de bot do Telegram Java EE.

O Objetivo desta API é permitir seu uso em containers Java EE.
Para criar um bot basta implementar a interface ```JBugBrasilLongPoolingBot``` Injetar o ```UpdatesReceiver``` e o ```OutcomeMessageProcessor```.

O UpdatesReceiver irá receber toda nova mensagem trocada e o OutcomeMessageProcessor irá processar a mensagem
com base nos plugins e serviços carregados no bot. Veja um Exemplo:

```java
@ApplicationScoped
public class MyBot implements JBugBrasilLongPoolingBot {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private UpdatesReceiver receiver;

    @Inject
    private OutcomeMessageProcessor msg;

    @Inject
    private MessageSender reply;

    @Override
    public void onUpdateReceived(MessageUpdate update) {
        msg.reply(update);
    }

    public void start() {
        receiver.start();
    }

    public void stop() {
        receiver.interrupt();
    }

}
```

Em breve estará disponível uma documentaçção completa da API bem como criar novos plugins, comandos ou serviços.

### Encontrou bugs ou tem alguma sugestão?
Não hesite em nos procurar, registre um issue ou nos envie um email: contato@jbugbrasil.com.br