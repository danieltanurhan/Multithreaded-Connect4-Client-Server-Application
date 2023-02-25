# Multithreaded-Connect4-Client-Server-Application
This project includes a custom made client server application-level protocol that is capable of syncing data for up to 200 clients to play the classic game of Connect4.

## Features

- Built with core Java and Swing
- Host up to 200 players on a server
- Play solo against a simple algorithm
- PVP to play against one other player
- Multi person chat


## Run

- Open both Client.java and Server.java on a IDE or
- To run using command line, first go to file locations
- use command $ javac Server.java to compile and $ java Server.java to run
- Then we do the same the Client file $ javac Client.java to compile and $ java Client.java to run
- Server address is taken as a string with no port required,example: localhost or 192.168.1.99


## Screenshots
- Loading Screen to connect to server                                                   
![client server connection login](https://user-images.githubusercontent.com/32969802/221377969-d462a033-b292-42df-a274-e6f5dc9397ed.png)
- Request username after making connection to server
![client server request username](https://user-images.githubusercontent.com/32969802/221377974-a53e05f5-4a08-476d-b9b7-30242ff5c18b.png)
- Join into PVP and start playing once enough players (max 2)
![client server two player example](https://user-images.githubusercontent.com/32969802/221377978-e4afa047-afbf-45e3-8bda-5a5600151ba5.png)
- Play by yourself againts a simple algorithm
![client server vs ABBOT](https://user-images.githubusercontent.com/32969802/221377982-63f0c80b-7c63-40f9-8692-15ce3b92ffa4.png)
