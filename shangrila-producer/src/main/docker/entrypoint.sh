#!/bin/sh

echo "The application will start in ${SHANGRILA_SLEEP}s..." && sleep ${SHANGRILA_SLEEP}
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/app.jar" "$@"
