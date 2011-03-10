SipRegistration CDI Example
---------------------------

This example demonstrates the use of CDI within the Sip Servlets context. 

-SipRegistar:
	A Singleton(JSR330 Singleton marks a type the injector will only instantiate once) class that is the injected module that will actually take care of the registration. Will also put the request URI in the "users" map (that will 		be available in the CDI's contexts as a bean to be injected) 

-SipRegistarClient:
	A Servlet 3.0 that injects the SipRegistar and the "users" map, in order to handle registration request. Should be deployed to MSS 2.x on Tomcat container

-SipRegistarClient:
	A Servlet 2.5 that injects the SipRegistar and the "users" map, in order to handle registration request. Should be deployed to MSS 1.x on Tomcat container

-SipRegistarClientAS5:
	A Servlet 2.5 that uses the SipRegistar and "users" map to handle registration requests. Should be deployed to MSS 1.x on JBoss AS5 container


How to build
-------------

For SipRegistarClient and SipRegistarClient25, on the top directory simply issue: mvn clean compile package
The war files will be SipRegistarClient/target/SipRegistarClient.war & SipRegistarClient25/target/SipRegistarClient25.war accordingly.
Deploy them to the container as usual.


For the SipRegistarClientAS5, inside the directory of the project issue: mvn clean compile package
The war files will be SipRegistarClientAS5/target/SipRegistarClientAS5.war
Deploy them to the container as usual.

Don't forget to apply dar configuration.

---
