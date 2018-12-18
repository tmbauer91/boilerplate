# boilerplate
This repo is intended to be a simple RESTful service to take mission files from various video games submitted for upload by a front end and store them in a Mongo database. Once stored in the Mongo, the user has the option to retrieve them, and/or edit them by adding additional briefing materials in the form of .pngs to the file. Ultimately files will be tied to specific users, who will have the option to keep them private or share them with others.

## Motivation
I lead a group of players that meet on a weekly basis to develop skills and fly "serious" missions in Digital Combat Simulator. As part of the process of organizing and briefing each mission, participants requested a more coherent way to review the mission plan during the flight without resorting to alt-tabbing and reviewing the briefing document. The file format for the DCS mission includes functionality to include supplemental briefing material in the form of .png files, which are then visible to players via an aircraft kneeboard in game. This repo is an an attempt to make an easy way for users to upload files, add whatever supplemental briefing material desired, and then make those files available for other users. The .pbo format associated with the infantry simulator Arma is also included for convienience.

## Tech/framework used

Built with
* Spring Boot
* Spring Rest
* Swagger
* MongoDb
* Gradle

## Installation
Download and unpack the zip into your work space. Once placed, build via:
gradlew build && java -jar build/libs/com.boilerplate-0.0.1.jar

When the repo is running, check Swagger for available options at:
http://localhost:8080/swagger-ui.html#/
