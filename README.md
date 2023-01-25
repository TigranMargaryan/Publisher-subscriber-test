# Publisher-subscriber Application
Publisher subscriber application with API implementation


### Overview

The application consists of three main components:

`PublisherService`: Responsible for scheduling the publishing of messages to a message queue.

`SubscriberService`: Responsible for listening to the message queue and saving the messages to the subscriber's list of messages.

`PubSubController`: Responsible for handling the API endpoints for adding and removing subscribers, and fetching messages for a subscriber.

##API Endpoints
### Add subscriber
`/api/subscribers`

`POST`: Adds a new subscriber to the system.

Request Body:

```json
{
  "name": "TestName"
}
```
### Delete subscriber
`/api/subscribers/{id}`

`DELETE`: Removes a subscriber from the system.

id -> subscriber id

### Get all messages for subscriber
`/api/subscribers/{id}/messages`

GET: Fetches all messages for a subscriber.

Params: 
```json
{
  "createdDate": "2022-01-01",
  "sort": "asc",
  "pageNo": "1",
  "pageSize": "10"
}
```
### Message

`/api/messages`

`POST` Add a message to the queue

Request Body:

```json
{
  "type": "messageType",
  "data": "messageContent"
}
```

### Broadcast messages
`/api/messages/broadcast`

`POST` Broadcast messages for subscribers

Request Body:

```json
{
  "name": "subscriberName"
}
```

### Note
The API is protected with an API key, which must be included in the request header as `apiKey`. Like  `header-key` `apiKey` && `header-value` `secretKey`

### Main profile is `dev`

#### For full configuration inside the envirement variables need to add some values

`MYSQL_DB_USERNAME` = {mysql username};

`MYSQL_DB_PASSWORD` = {mysql password};

`apiKey`= {secret key}

## Integration Test
Integration tests for the application can be found in the `src/test/java/com/example/publishsubscribe` package. 

## Steps to work with application

1. First need to create the subscriber
2. Need to create messages (after that every 5 second created messages will be added in the queue, but not in the database)
3. Need to call broadcasts endpoint for subscriber (it will assign message for subscriber and will save messages in the database)
4. With get messages endpoint it will get by default 5 messages for subscriber
5. Delete subscriber

#### Inside `postman-script` directory you can find created endpoints, which can be imported into the postman app 