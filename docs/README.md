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

* **dataSource** : An object implementing [DataSource](https://docs.oracle.com/javase/7/docs/api/javax/sql/DataSource.html)
* **dialect** : An object implementing [ISQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/dialect/ISQLDialect.java). The ORM delegates to this object each time it needs to build a SQL statement. Defaults to [GenericSQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/dialect/GenericSQLDialect.java). Subclass to provide support for your own database.
* **mappingProvider** : An object implementing [IORMappingProvider](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/provider/IORMappingProvider.java). The ORM delegates to this object each time it needs to build the [mapping](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/ORMapping.java) between a class and a database table. Defaults to [JPAORMappingProvider](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/provider/JPAORMappingProvider.java). Subclass if you need a mapping for a class that isn't based on JPA annotations.
* **typeMapper** : An object implementing [IJDBCTypeMapper](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/type/IJDBCTypeMapper.java). The ORM delegates to this object each types it needs to convert a value coming from the database to a Java object. Default to [DefaultJDBCTypeMapper](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/mapping/type/DefaultJDBCTypeMapper.java).
* **executor** : An object implementing [IStatementExecutor](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/executor/IStatementExecutor.java). The ORM delegates to this class each time it needs to execute a statement. Defaults to [DefaultStatementExecutor](https://github.com/rlemaigre/JMiniORM/blob/master/main/java/org/jminiorm/executor/DefaultStatementExecutor.java).

# JPA annotations

## Supported annotations
This project partially supports a subset of the standard JPA 2.1 annotations, namely :

* [Table](https://docs.oracle.com/javaee/7/api/javax/persistence/Table.html) : Specifies the table for the annotated entity.
* [Index](https://docs.oracle.com/javaee/7/api/javax/persistence/Index.html) : Specifies an index to setup when the table of an entity is created.
* [Column](https://docs.oracle.com/javaee/7/api/javax/persistence/Column.html) : Specifies the column for a persistent property.
* [Id](https://docs.oracle.com/javaee/7/api/javax/persistence/Id.html) : Specifies the primary key of an entity.
* [GeneratedValue](https://docs.oracle.com/javaee/7/api/javax/persistence/GeneratedValue.html) : Specifies that the property is database generated in insert. 
* [Convert](https://docs.oracle.com/javaee/7/api/javax/persistence/Convert.html) : Specifies the converter class to use to convert between java value and database value.
* [Transient](https://docs.oracle.com/javaee/7/api/javax/persistence/Transient.html) : Specifies that the property isn't persisted.

## Example of an annotated class
This class is used as an example for the rest of this doc.

``` java
@Table(name = "users", indexes = {
        @Index(name = "login_index", columnList = "login ASC")
})
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 16)
    private String login;
    private String password;
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    @Convert(converter = RolesJsonConverter.class)
    private List<Role> roles;

    public User() {
    }

    // ...getters and setters...

}

public class Role {

    private String name;

    public Role() {
    }

    // ... getters and setters...

}

public class RolesJsonConverter extends JsonAttributeConverter<List<Role>> {
    // Nothing to do here. The class is simply declared to capture de "List<Role>" type in a way that is available at
    // runtime for the JsonAttributeConverter parent class.
}
```























