# Anno Search
Semantic Annotation Driven search engine offering faceted search and named entity highlighting in search results content.

## Used techonologies

The webapp is based on Spring Boot (backend) and Angular 2 (frontend). It 
uses Apache Lucene for indexing and full text search.

## Build
This is a Maven based project, so to build it execute the following command:
```
mvn clean install
```

## Execution
Frontend and backend are separate services so you need to start them separately.
To start the frontend execute:
```
cd anno-search-ui
npm start
```
To start the backend execute:
```
cd anno-search-server
mvn spring-boot-run
```
The webapp will be available on *http://localhost:4200*.

