#!/bin/bash
####################################################################################
# Este script é integrado com o systemctl para controle do ciclo de vida do bot    #
#                                                                                  #
####################################################################################
# Uso:                                                                             #
# sudo systemctl enable|start|stop|restart jbug-brasil                             #
#                                                                                  #
####################################################################################
# Parâmetros                                                                       #
# start|stop|restart                                                               #
#                                                                                  #
####################################################################################
# Versão                                                                           #
# 1.0.Final                                                                        #
#                                                                                  #
####################################################################################

# All the variables will come from the file /opt/bot/jbug-brasil.conf

case $1 in
"start")
  echo "Starting jbug-brasil bot."
  $JAVA_HOME/bin/java -jar -Dbr.com.jbugbrasil.telegram.token=${BOT_TOKEN} -Dbr.com.jbugbrasil.telegram.userId=${BOT_USER_ID} -Dbr.com.jbugbrasil.telegram.chatId=${BOT_CHAT_ID} -Dbr.com.jbugbrasil.gitbooks.token=${GITBOOKS_TOKEN} telegram.bot-3.1.Final-swarm.jar &
  echo $! > /opt/bot/jbug-brasil.pid
  ;;
"restart")
  echo "Restarting jbug-brasil bot."
  $JAVA_HOME/bin/java -jar -Dbr.com.jbugbrasil.telegram.token=${BOT_TOKEN} -Dbr.com.jbugbrasil.telegram.userId=${BOT_USER_ID} -Dbr.com.jbugbrasil.telegram.chatId=${BOT_CHAT_ID} -Dbr.com.jbugbrasil.gitbooks.token=${GITBOOKS_TOKEN} telegram.bot-3.1.Final-swarm.jar &
  echo $1 >  /opt/bot/jbug-brasil.pid
  ;;
"stop")
  echo "Stopping jbug-brasil bot."
  kill -15 `cat /opt/bot/jbug-brasil.pid`
  em -rf  /opt/bot/jbug-brasil.pid
  ;;
*)
  echo "Parâmetro não é válido, utilize start, restart ou stop."
  ;;
esac
