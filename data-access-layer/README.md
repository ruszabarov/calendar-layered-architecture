# Development

Follow the following steps to start developing the data access layer.
Please note: the following directions show steps **only** for IntelliJ IDEA.

1. Install Java 21
2. Set up environement variables
    1. Preferences -> Build, Execution, Deployment -> Build Tools -> Maven -> Runner
    2. Set up `DB_IP`, `DB_USERNAME`, and `DB_PASSWORD` variables
3. run `mvn install`

# Production

Follow the following step to start the data access layer for production

1. Run `mvn package`
2. Run the jar `java -jar /path/to/jar`
3. Application will be available on [localhost:8080](http://localhost:8080)
4. Schema definition available on [/swagger-ui](http://localhost:8080/swagger-ui/index.html#/)