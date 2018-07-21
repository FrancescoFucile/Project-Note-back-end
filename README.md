# Project-Note-back-end
Back-end for project note.

Online document sharing platform designed to share notes.
Utents are able to upload notes as images from their PCs or from the cloud with Google Drive via OAuth autentication.
Notes are classified by their uploaders with a system of tags, which can then be used to search shared them.
Users can read and change pages directly from the browser as pages are stramed from the database via websocket.

The system is composed by 4 modules:
-   front-end code to manage session data and visualization
-   back-end server used during note serach/visualization
-   back-end server used during the upload
-   database used to store files and metadata

The server is written in Scala on top of the Akka actors/stream/http framework, which is used to provide a strong support for asynchronous communication.

Webpages, search and uploads relie on HTTP/REST to provide easy access from the web.

Messages between server and database are sent via AMQP with RabbitMQ in order to provide support for asynchronous communication and      load tolerance.

Note browsing (next/previous page) is accomplished with the use of websocket in order to guarantee a steady connection as the reading    session can take some time. Two websockets are open during the connection, one with the server to send the request (wich are then sent    to the DB via AMQP) and one with the DB which sends the pages in base64 format. This approach is meant to avoid files to pile up in      the server and at the same time taking advantage of asynchronous communication to avoid overload related issues on the DB, as file        exchange can generate heavy traffic loads.

Server API:
- https://project-note-main-server.herokuapp.com/         GET homepage - search form
- https://project-note-main-server.herokuapp.com/search   GET search (takes parameters)
- https://project-note-main-server.herokuapp.com/upload   GET redirects to the upload page
- https://project-note-main-server.herokuapp.com/ws       GET used to open websocket connection with the server

Upload of one or multiple pages (as images) can be done locally or via OAuth from google drive.
Uploads are manageb by another microservice (server) using Google APIs for the OAuth authentication.
Uploads make use of tags to allow users to find and sort notes.

Database repo: https://gist.github.com/biosan/bdc89141b9acf9bb09bf43b34606ad0f

Upload service repo:

Authors: 

Francesco Fucile, 
Lucia Esposito, 
Alessandro Biondi
