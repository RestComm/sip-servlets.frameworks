SipRegistration CDI Example
---------------------------

This example demonstrates the use of CDI within the Sip Servlets context. 


-SipRegistarClient:
	A Servlet 3.0 that injects the SipRegistar and the "users" map, in order to handle registration request. 
	Should be deployed to MSS 2.x on Tomcat container

-SipRegistarClient25:
	A Servlet 2.5 that injects the SipRegistar and the "users" map, in order to handle registration request. 
	Should be deployed to MSS 1.x on Tomcat container or JBoss AS5


How to build
-------------

For each project simply issue: mvn clean compile package
The war files will be ${Project}/target/${Project}.war.
Deploy them to the container as usual.

Specially for SipRegistarClient25, in order to build for JBoss AS5: mvn clean compile package -Pjboss5

Don't forget to apply dar configuration.

---
