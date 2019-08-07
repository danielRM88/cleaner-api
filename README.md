# cleaner-api

To run the application just clone the github repo, go inside the folder and run:

`mvn clean spring-boot:run`

After that you can access the api using the following endpoint to optimize cleaners assignment:

POST `http://localhost:8080/v1/cleaners/optimize`

Example json request:

`{ "rooms": [24, 28], "senior": 11, "junior": 6 }`

And response:

`[ { "senior": 2, "junior": 1 }, { "senior": 2, "junior": 1 } ]`
