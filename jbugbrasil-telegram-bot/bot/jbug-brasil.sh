#!/bin/bash

# The MIT License (MIT)
#
# Copyright (c) 2017 JBug:Brasil <contato@jbugbrasil.com.br>
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of
# this software and associated documentation files (the "Software"), to deal in
# the Software without restriction, including without limitation the rights to
# use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
# the Software, and to permit persons to whom the Software is furnished to do so,
# subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
# FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
# COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
# IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
# CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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

VERSION="1.1-SNAPSHOT"

case $1 in
"start")
  echo "Starting jbug-brasil bot."
  $JAVA_HOME/bin/java -jar -Xms150m -Xmx300m -XX:MetaspaceSize=100m -Djava.io.tmpdir=/opt/bot/tmp -Dbr.com.jbugbrasil.bot.telegram.token=${BOT_TOKEN} -Dbr.com.jbugbrasil.bot.telegram.userId=${BOT_USER_ID} -Dbr.com.jbugbrasil.bot.telegram.chatId=${BOT_CHAT_ID} -Dbr.com.jbugbrasil.bot.gitbook.token=${GITBOOKS_TOKEN} telegram-bot-${VERSION}-swarm.jar &
  echo $! > /opt/bot/jbug-brasil.pid
  ;;
"restart")
  echo "Restarting jbug-brasil bot."
  $JAVA_HOME/bin/java -jar -Xms150m -Xmx300m -XX:MetaspaceSize=100m -Djava.io.tmpdir=/opt/bot/tmp -Dbr.com.jbugbrasil.bot.telegram.token=${BOT_TOKEN} -Dbr.com.jbugbrasil.bot.telegram.userId=${BOT_USER_ID} -Dbr.com.jbugbrasil.bot.telegram.chatId=${BOT_CHAT_ID} -Dbr.com.jbugbrasil.bot.gitbook.token=${GITBOOKS_TOKEN} telegram-bot-${VERSION}-swarm.jar &
  echo $! >  /opt/bot/jbug-brasil.pid
  ;;
"stop")
  echo "Stopping jbug-brasil bot."
  kill -15 `cat /opt/bot/jbug-brasil.pid`
  rm -rf  /opt/bot/jbug-brasil.pid
  ;;
*)
  echo "Parâmetro não é válido, utilize start, restart ou stop."
  ;;
esac
