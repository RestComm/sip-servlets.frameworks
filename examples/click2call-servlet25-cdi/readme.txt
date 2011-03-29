This version of the Click-To-Call incorporates Mobicents CDI integration and is suitable for Servlet 2.5 container.
The CDI container is responsible to inject and make available these beans. 

Building and Deployment
-----------------------

To build run:
mvn clean compile war:war

And deploy the generated war file to MSS 1.x on Tomcat.

 
If you want to REGISTER use this as a DAR file:
REGISTER:("org.mobicents.servlet.sip.example.SimpleAsyncApplication", "DAR:From", "ORIGINATING", "", "NO_ROUTE", "0")
OPTIONS:("org.mobicents.servlet.sip.example.SimpleAsyncApplication", "DAR:From", "ORIGINATING", "", "NO_ROUTE", "0")
