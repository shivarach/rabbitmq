# rabbitmq
* Playing with rabbitmq. Find basic concepts at [amqp-concept](https://www.rabbitmq.com/tutorials/amqp-concepts.html)
* Workers(Worker1 and Worker2) will wait for messages to consume and publishers(NewTaskPublisher and NewTaskPublisher2) publishes messages
* Assumes RabbitMQ server runs on localhost with default port 5672

## Build
`mvn clean install`

## Run (from Intellij)
* Run Worker (run once, will listen for messages)
* Run Worker2  (run once, will listen for messages)
* Run NewTaskPublisher (can run multiple times)
* Run NewTaskPublisher2 (can run multiple times)

