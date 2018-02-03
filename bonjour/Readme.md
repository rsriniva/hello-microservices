# Bonjour
Simple microservice using Spring Boot

Build and Deploy bonjour locally
-----------------------------

1. Open a command prompt and navigate to the root directory of this microservice.
2. Type this command to build the application:

        mvn clean package

3. This will create a uber jar at  `target/bonjour-1.0.jar`.
4. Run the fat jar

        java -jar target/bonjour-1.0.jar

4. The application will be running at the following URL: <http://localhost:8080/api/bonjour>

Deploy the application in OpenShift
-----------------------------------

1. Make sure to be logged into OpenShift.
2. Make sure you have created or selected the appropriate OpenShift project.
3. Execute:

		mvn clean fabric8:deploy
