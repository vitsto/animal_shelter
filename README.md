# Animal shelter

## What the project does

The project is a Spring-Boot Java-based application integrated with the Telegram-bot and created for animal shelter needs.

## Why the project is useful

This application allows processing requests from potential and current pets’ keepers. Using Telegram-bot people may get different information about the shelter, dogs and cats, contact with volunteers and send them a weekly report about a pet.

## How users can get started with the project

To use this application you should install [Telegram Desktop](https://desktop.telegram.org/?setln=ru), [PostgreSQL Database](https://www.postgresql.org/download/) and [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows) with Maven Build system.

1) Create a database for your shelter (e.g. via SQL Shell).
2) Connect this database with Spring Boot application: 
- choose 'Database' on the right pannel in IDEA 

![Снимок экрана (1243)](https://github.com/vitsto/animal_shelter/assets/111565371/a9afdb0d-9994-4ca4-aabd-fe1cdc69e954)

- choose 'PostgreSQL' in Datasources and enter your username, password and database name:

![Снимок экрана (1245)](https://github.com/vitsto/animal_shelter/assets/111565371/7b70cc81-3a25-4110-84ca-55de21c06886)

3) To create tables in the database you need to write SQL-scripts

## Who maintains and contributes to the project

The application was written by Stolyarov Vitaliy, Scherbakov Anton and Fomina Marina. This is a team training project within [Java-developers](https://sky.pro/courses/programming/java-developer) studying process at [Skypro online university](https://sky.pro/).

## Demo


## Technologies

In this project developers used:
- Java 17
- Spring Boot/Web/Test
- PostgreSQL
- Liquibase
- [Pengrad (Telegram Bot API)](https://github.com/pengrad/java-telegram-bot-api)
- JUnit 5, Mockito
