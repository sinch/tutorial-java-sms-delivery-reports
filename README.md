# Tutorial: Receive delivery reports in a Spring Boot application

This is the supporting code for the [Receiving Delivery Reports in Spring Boot](https://sinch.readme.io/docs/tutorial-java-spring-boot-delivery-reports) Sinch tutorial.

The project uses an Ngrok tunnel to expose a Spring Boot application publicly, and uses the Ngrok tunnel's URL as the callback to receive delivery reports for sent SMS messages.

There are two endpoints:

1) `POST /sms/send`
   Send an outbound SMS with delivery reports enabled and pointing to our application.
1) `POST /sms/deliveryReport`
   Callback endpoint that will be called by the Sinch REST API to send delivery reports.

## How to Run?

There are two ways to run this project:

1) [Manually](#run-manually): by installing dependencies, compiling and running locally
1) [With Docker](#run-with-docker): quickest way if you already have Docker installed.

### Run manually

 1) [Install Ngrok](http://ngrok.com/download)
 1) Run Ngrok: `ngrok http 8080`
 1) Configure your Sinch API Token and Service Plan ID in the `application.yml` file at the root of the project.
 1) Run the Spring Boot application: `./gradlew bootRun`
 
### Run with Docker

 1) Configure your Sinch API Token and Service Plan ID in the `.env` file
 1) Run `docker-compose up`. This will build and start 2 containers, one for Ngrok and the other for the pre-packaged (see `/bin`) Spring Boot application.
 
## Send an SMS message

Once both Ngrok and the Spring Boot application are running (see above), you can send an SMS message by calling the `POST /sms/send` endpoint:

```shell
curl -X POST \
  http://localhost:8080/sms/send \
  -H 'Content-Type: application/json' \
  -d '{
	"message": "Hello from Sinch!",
	"recipients": [
		"{DESTINATION_PHONE_NUMBER}"	
	]
}'
```

Do not forget to replace the `{DESTINATION_PHONE_NUMBER}` placeholder with your actual phone number.

A few seconds after the SMS is received on your handset, you should see a similar log output in the Spring Boot application:

```
Received delivery report: BatchDeliveryReport{batchId=lMclWkZ_SKelDrk-, totalMessageCount=1, statuses=[Status{code=0, status=DeliveryStatus{status=Delivered}, count=1, recipients=[]}]}
``` 

Congratulations, you've successfully processed a Sinch delivery report callback from a Spring Boot application!
