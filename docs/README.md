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
* Transactions support
* Raw select statements with custom return types (primitive, Map, Object)
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

* **dataSource** : A class implementing [DataSource](https://docs.oracle.com/javase/7/docs/api/javax/sql/DataSource.html)
* **dialect** : A class implementing [ISQLDialect](org/jminiorm/dialect/ISQLDialect.java). The ORM delegates to this object each time it needs to build a SQL statement. Defaults to [GenericSQLDialect](org/jminiorm/dialect/GenericSQLDialect.java).
* **mappingProvider** : A class implementing [IORMappingProvider](org/jminiorm/mapping/provider/IORMappingProvider.java). Defaults to [JPAORMappingProvider](org/jminiorm/mapping/provider/JPAORMappingProvider.java)
* **typeMapper** : A class implementing [IJDBCTypeMapper](org/jminiorm/mapping/type/IJDBCTypeMapper.java). Default to [DefaultJDBCTypeMapper](org/jminiorm/mapping/type/DefaultJDBCTypeMapper.java)
* **executor** : A class implementing [IStatementExecutor](org/jminiorm/executor/IStatementExecutor.java). Defaults to [DefaultStatementExecutor](org/jminiorm/executor/DefaultStatementExecutor.java)
