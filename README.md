# Spring FileService Example

Example Project to demonstrate file upload/download with Spring. It's mainly used as example code for a blog post but can be used and 
adapted into a production ready service.

## Configuration

The important settings are in `application.yml`:

    spring:
        datasource:
            driver-class-name: org.h2.Driver
            url: jdbc:h2:file:./target/database;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;
            username: sa
            password: pw
    
    save-dir: ./target/files

Since this is a demo application it uses the H2 DB in Postgres Mode. The database as well as the downloaded files are both 
stored in the `target` directory, so to start with a clean slate you can just do a `mvn clean`.

## Running

The service can be started by just running the main entrypoint in FileServiceApplication. It will start and present a very simple
user interface on http://localhost:8080 where you can upload a file. After an upload the file should show up in the list of files 
and can be downloaded.  