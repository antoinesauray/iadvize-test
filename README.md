# Backend test for iAdvize
Hi there ! My name is Antoine and this is my technical test regarding my application at [iAdvize](https://www.iadvize.com/fr/)

[Demo](http://demo.sauray.com/api)

## What's included ?
* Docker compose for running both the docker image of the API and the RDMS
* Tests
* Swagger v1.2 specification on /swagger

## Theme
The test consists of parsing data from "Vie de merde" and making it available through a rest API.

## Programming languages
I used **Scala** for the API. The main reason is because that is the language I would be at iAdvize. I also used **Python** to write a scrapper and parser file.

## Storage
I used a **PostGreSQL** database for storing the data. At first I planned on using SQlite, but it turns out the support of Scala Slick for SQlite type is a bit chaotic (especially for date/time). I decided to switch to PGSQL because it is a common production RDMS. I did not use MongoDB because nowadays, it is not a viable option anymore (less efficient than PGSQL to treat JSON)

## How to use
```bash 
docker-compose up
```
The API will be exposed on your port **8081**, and the database will be exposed on port **5554**.
Now the API is empty. To fill in data, execute the following script.
```bash 
python3 parser/parse.py
```
The script is a bit long because VDM has a lot of doubles, sometimes triples. It makes sure it does not insert twice the same content. The data will appear progressively in the API.

API Docker image => https://hub.docker.com/r/antoinesauray/backend-test-iadvize/

Hope you are happy with this :)
