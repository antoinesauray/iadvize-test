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
I used **Scala** for the API. The main reason is because that is the language I would use at iAdvize. I also used **Python** to write a scrapper and parser file.

## Storage
I used a **PostGreSQL** database for storing the data. At first I planned on using SQlite, but it turns out the support of Scala Slick for SQlite type is a bit chaotic (especially for date/time). I decided to switch to PGSQL because it is a common production RDMS. I did not use MongoDB because nowadays, it is not a viable option anymore (less efficient than PGSQL to store JSON)

## How to use
```bash 
docker-compose up
```
The API will be exposed on your port **8081**, and the database will be exposed on port **5554**.
Now the API is empty. To fill in data, execute the following script.
```bash 
python3 parser/parse.py
```
The script has two dependencies.
* bs4
* psycopg2

It is a bit long because VDM has a lot of doubles, sometimes triples. It makes sure it does not insert twice the same content. The data will appear progressively in the API.

API Docker image => https://hub.docker.com/r/antoinesauray/backend-test-iadvize/

## Building the image yourself
First you need to set the configuration. Modifiy the application.conf (src/main/resources/) files with the appropriate parameters. Then execute the following command.
```bash 
sbt docker
```
The tests will execute prior to building the image. If they fail, make sure the application.conf is ok, if it is, feel free to report an issue.

That's it ! The image is built and you can start using it.
Hope you are happy with this :)
