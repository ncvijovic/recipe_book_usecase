FROM openjdk:17-slim

RUN apt-get update && apt-get install -y --no-install-recommends curl && apt-get clean && rm -rf /var/lib/apt/lists/*
RUN curl -fL https://github.com/krallin/tini/releases/download/v0.19.0/tini-static -o /usr/bin/tini && chmod +x /usr/bin/tini

ARG VERSION=0.0.1-SNAPSHOT
ARG SERVICENAME=recipebook
ARG MAINTAINER=nebojsa.cvijovic@valcon.com

LABEL maintainer="$MAINTAINER"
LABEL servicename="$SERVICENAME"
LABEL version="$VERSION"

RUN adduser --system "$SERVICENAME"  && mkdir /log  && chown "$SERVICENAME" /log

USER "$SERVICENAME"

COPY build/libs/"$SERVICENAME"-"$VERSION".jar app.jar

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
ENTRYPOINT ["/usr/bin/tini", "--"]