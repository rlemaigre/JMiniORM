# Overview
JMiniORM is a lightweight ORM and database utility for the Java language. It has no dependencies, its footprint is small (~100 KB) and it is very easy to learn.

# Features
* Simple config in Java (no XML files)
* [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) support for annotated classes
* Raw select statements with custom return types (primitive, Map, Object)
* Uses standard [JPA](https://en.wikipedia.org/wiki/Java_Persistence_API) annotations
    * For object relational mapping
    * For SQL schema generation (CREATE TABLE statements)
* JPA [attribute converter](https://docs.oracle.com/javaee/7/api/javax/persistence/AttributeConverter.html) support
    * Ships with a JSON attibute converter    
* Transactions support
* Pluggable SQL dialects

# Configuration
At the very least, you must provide the database URL, username and password.
```java
IDatabaseConfig config = new DatabaseConfig.Builder()
                .dataSource("jdbc:h2:mem:test-single;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;", "", "")
                .build();
IDatabase db = new Database(config); 
```