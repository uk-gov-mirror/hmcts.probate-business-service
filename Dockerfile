FROM openjdk:8-alpine

RUN mkdir -p /usr/local/bin

COPY docker/entrypoint.sh /
COPY build/libs/business-service*.jar /business-service.jar

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy= curl --silent --fail http://localhost:8081/health

EXPOSE 8081

ENTRYPOINT [ "/entrypoint.sh" ]
