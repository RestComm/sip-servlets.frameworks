This version of the Click-To-Call incorporates Mobicents CDI integration and is suitable for Servlet 2.5 container.
The CDI container is responsible to inject and make available these beans. 

Building and Deployment
-----------------------

To build run:
mvn clean compile war:war

And deploy the generated war file to MSS 1.x on Tomcat.

In order to deploy to JBoss AS5:
mvn clean compile war:war -Pjboss5
