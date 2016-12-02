# Protecta
This is the android application part of a mobile anti-theft application.

The application comprises of three different parts.
1. Android Application
2. Web Application
3. Windows Form Application

Description: The Main idea of this application was to track the location of the mobile phone using the gps sensor. Functionality which differentiated this application from other apps was that it doesn't only send the location of the mobile phone to the server using the internet but it could also be sent through mobile SMS. Other funtionalites included remotely locking the mobile phone with a password, wiping the data from the phone and factory reset. Google Cloud Messaging api was used in this application in order to remotely connect with the application from the server. All the actions were performed from the web application. The user could sign up from the mobile application and later on login from the website using the same credentials. Which were used to perform action on the mobile phone and locating the mobile phone as well.
The C# based windows form application was used as a bridge between the web application and the mobile phone using the GSM modem. Windows application was specifically designed to carry out GSM network based communication between the mobile application and the server.
