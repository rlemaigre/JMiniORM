# Overview
JMiniORM is a lightweight ORM and database utility for the Java language. It has no dependencies, its footprint is small (~100 KB) and it is very easy to learn.

# Features
* Simple config in Java (no XML files)
* Uses standard [JPA](https://en.wikipedia.org/wiki/Java_Persistence_API) annotations
    * For object relational mapping
    * For SQL schema generation (CREATE TABLE statements)
* [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) support for annotated classes
* JPA [attribute converter](https://docs.oracle.com/javaee/7/api/javax/persistence/AttributeConverter.html) support
    * Ships with a JSON attibute converter
* Raw select statements with custom return types (primitive, Map, Object)    
* Transactions support
* JDBC batch mode support

* Pluggable SQL dialects

# Configuration
At the very least, you must provide the database URL, username and password.

``` java
IDatabaseConfig config = new DatabaseConfig.Builder()
                .dataSource("<url>", "<username>", "<password>")
                .build();
IDatabase db = new Database(config);
```

If you do so, a [HikaryCP](https://brettwooldridge.github.io/HikariCP/) connection pool is used. 

Optional parameters :

* **dataSource** : A object implementing [DataSource](https://docs.oracle.com/javase/7/docs/api/javax/sql/DataSource.html)
* **dialect** : A object implementing [ISQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/dialect/ISQLDialect.java). The ORM delegates to this object each time it needs to build a SQL statement. Defaults to [GenericSQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/dialect/GenericSQLDialect.java). Subclass to provide support for your own database.
* **mappingProvider** : A class implementing [IORMappingProvider](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/provider/IORMappingProvider.java). The ORM delegates to this object each time it needs to build the [mapping](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/ORMapping.java) between a class and a database table. Defaults to [JPAORMappingProvider](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/provider/JPAORMappingProvider.java). Subclass if you need a mapping for a class that isn't based on JPA annotations.
* **typeMapper** : A class implementing [IJDBCTypeMapper](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/type/IJDBCTypeMapper.java). The ORM delegates to this object each types it needs to convert a value coming from the database to a Java object. Default to [DefaultJDBCTypeMapper](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/type/DefaultJDBCTypeMapper.java).
* **executor** : A class implementing [IStatementExecutor](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/executor/IStatementExecutor.java). The ORM delegates to this class each time it needs to execute a statement. Defaults to [DefaultStatementExecutor](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/executor/DefaultStatementExecutor.java).
