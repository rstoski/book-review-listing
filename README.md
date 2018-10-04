Due to Spring Framework bug SPR-15859 ( https://jira.spring.io/browse/SPR-15859 ), the following JVM option must be included "--add-opens java.base/java.lang=ALL-UNNAMED"

Project can be run with:
mvn package && java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/book-review-listing-1.0-SNAPSHOT.jar
