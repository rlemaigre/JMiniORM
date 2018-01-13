[![Build Status](https://travis-ci.org/rlemaigre/JMiniORM.svg?branch=master)](https://travis-ci.org/rlemaigre/JMiniORM) [![Coverage Status](https://coveralls.io/repos/github/rlemaigre/JMiniORM/badge.svg?branch=master)](https://coveralls.io/github/rlemaigre/JMiniORM?branch=master)

# Overview
JMiniORM is a lightweight ORM and database utility for the Java language. It has no dependencies, its footprint is small (~100 KB), it is extensible and it is very easy to learn.

# Features
* Simple config in Java (no XML files)
* Uses standard [JPA](https://en.wikipedia.org/wiki/Java_Persistence_API) annotations
    * For object relational mapping
    * For SQL schema generation (CREATE TABLE statements)
* [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) support for annotated classes
* JPA [attribute converters](https://docs.oracle.com/javaee/7/api/javax/persistence/AttributeConverter.html) support
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
* **dialect** : An object implementing [ISQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/dialect/ISQLDialect.java). The ORM delegates to this object each time it needs to build a SQL statement. Defaults to [GenericSQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/dialect/GenericSQLDialect.java). Subclass to provide support for your own database.
* **mappingProvider** : An object implementing [IORMappingProvider](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/mapping/provider/IORMappingProvider.java). The ORM delegates to this object each time it needs to build the [mapping](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/mapping/ORMapping.java) between a class and a database table. Defaults to [JPAORMappingProvider](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/mapping/provider/JPAORMappingProvider.java). Subclass if you need a mapping for a class that isn't based on JPA annotations.
* **typeMapper** : An object implementing [IJDBCTypeMapper](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/mapping/type/IJDBCTypeMapper.java). The ORM delegates to this object each time it needs to convert a value coming from the database to a primitive Java value. Default to [DefaultJDBCTypeMapper](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/mapping/type/DefaultJDBCTypeMapper.java).
* **executor** : An object implementing [IStatementExecutor](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/executor/IStatementExecutor.java). The ORM delegates to this class each time it needs to execute a statement. Defaults to [DefaultStatementExecutor](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/executor/DefaultStatementExecutor.java).

# JPA annotations

## Supported annotations
This project partially supports a subset of the standard JPA 2.1 annotations, namely :

* [Table](https://docs.oracle.com/javaee/7/api/javax/persistence/Table.html) : Specifies the table for the annotated entity.
* [Index](https://docs.oracle.com/javaee/7/api/javax/persistence/Index.html) : Specifies an index to setup when the table of an entity is created.
* [Column](https://docs.oracle.com/javaee/7/api/javax/persistence/Column.html) : Specifies the column for a persistent property.
* [Id](https://docs.oracle.com/javaee/7/api/javax/persistence/Id.html) : Specifies the primary key of an entity.
* [GeneratedValue](https://docs.oracle.com/javaee/7/api/javax/persistence/GeneratedValue.html) : Specifies that the property is database generated on insert. 
* [Convert](https://docs.oracle.com/javaee/7/api/javax/persistence/Convert.html) : Specifies the converter class to use to convert between java value and database value.
* [Transient](https://docs.oracle.com/javaee/7/api/javax/persistence/Transient.html) : Specifies that the property isn't persisted.

## Example of an annotated class
This class is used as an example throughout the rest of this doc.

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

    public User() {
    }

    // ...getters and setters...
}
```

# Queries

## ORM vs generic
Queries come in two flavours :

* "generic" : accept and return Maps, or allows for executing arbitrary SQL statement ("CREATE TABLE...").
* "ORM" : accept and return objects of JPA annotated classes.

Only ORM queries are described in this doc, except for the generic select query that can be used to execute arbitrary SELECT statements and retrieve the result in a type of your choosing.

## ORM queries

### Insert

``` java
// Creates a User :
User user = new User(...);

// Inserts it into the users table :
db.insert(user);

// The id generated by the database is now set :
assertNotNull(user.getId());

// Mass insert (beneficial if batch execution mode is activated) :
db.insert(<collection of users>);
```

### Delete

``` java
// By object (the id must be set) :
db.delete(user);

// By id :
db.delete(User.class, <id>);

// Mass delete (beneficial if batch execution mode is activated) :
db.delete(<collection of users>);
```

### Update

``` java
user.setLogin(<new login>)
db.update(user);

// Mass update (beneficial if batch execution mode is activated) :
db.update(<collection of users>);
```

### Select

``` java
// By id :
User user = db.select(User.class, <id>);

// With where, orderBy, limit, offset :
List<User> users = db.select(User.class)
        .where("login = 'test'")
        .orderBy("registration_date DESC")
        .limit(10)
        .offset(5)
    .list();

// First User of the result set :
User user = db.select(User.class).where(<where>).first();

// Single User of the result set (exception generated if 0 or more than one encountered) :
User user = db.select(User.class).where(<where>).one();
```

## Generic queries 

### Select
Sometimes it is necessary to execute a select statement that spans several tables (joins) or one that doesn't (necessarily) have a result set that maps to a class. Such queries can be executed that way :

``` java
// Result as maps :
List<Map<String,Object>> maps = db.select("select x, y, z from u, v where ...")
    .asMap()
    .list();

// Result as objects (JPA annotations are respected here too, as they are with ORM queries) :
List<SomeClass>> maps = db.select("select x, y, z from u, v where ...")
    .asObject(SomeClass.class)
    .list();

// Result as primitives :
List<Integer> db.select("select id from ...")
    .asPrimitive(Integer.class)
    .list();

// One and first are supported as well :
Long count = db.select("select count(*) from ...")
    .asPrimitive(Long.class)
    .one();
```

Notice that such queries don't start by specifying a Java class, thus the select and from clause can't be infered and must be provided explicitly.

### Raw
You can execute arbitrary SQL statements like so :

``` java
db.sql("DROP TABLE test");
```

# Transactions
The interface of transaction is the same as the one of database + commit, rollback and close. Once created, a transaction must be closed, otherwise the underlying connection won't be returned to the pool. Closing a transaction automatically rolls back any pending operations.

Tu ensure a block of SQL statements is either executed fully or not at all, do :

``` java
try (ITransaction transaction : db.createTransaction()) {
    // ...work with the transaction...
    transaction.commit();
}
```

# Utilities

The following utility class is provided to help manipulate result sets : [RSUtils](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/utils/RSUtils.java).

Example :

``` java
List<User> users = db.select(User.class).list();
Map<Integer, User> usersById = RSUtils.index(users, "id");
Map<LocalDate, List<User>> usersGroupedByRegistrationDate = RSUtils.group(users, "registrationDate");
Set<String> logins = RSUtils.distinct(users, "login");

```

# Schema generation
To create the database table and indexes for an entity according to its JPA annotations, use :

``` java
db.createTable(User.class);
```

# JDBC batch mode

[Batch mode](https://www.tutorialspoint.com/jdbc/jdbc-batch-processing.htm) is disabled by default. To enable it, configure the database this way :
 
``` java
IDatabaseConfig config = new DatabaseConfig.Builder()
                .dataSource("<url>", "<username>", "<password>")
                .statementExecutor(new BatchStatementExecutor())
                .build();
IDatabase db = new Database(config);
```

Batch mode can greatly improve performance for mass insertions/deletions/udpates, especially if the database and the application are on different machines. However, when used in combination with database generated keys, it may cause problems with some drivers : when executing a batch of inserts, some drivers only return the last generated key instead of the whole lot. Try it with your driver and see if it works.

# Logging

You can enable logging of SQL statements by configuring the database this way :
``` java
IDatabaseConfig config = new DatabaseConfig.Builder()
                .dataSource("<url>", "<username>", "<password>")
                .statementExecutor(new SLF4JLoggingStatementExecutor(new DefaultStatementExecutor()))
                .build();
IDatabase db = new Database(config);
```

# Custom SQL dialect
SQL dialects are objects implementing [ISQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/dialect/ISQLDialect.java) The project ships with a generic SQL dialect that tries to accomodate for most databases : [GenericSQLDialect](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/dialect/GenericSQLDialect.java). To account for the peculiarities of your own database, subclass it and configure it that way : 

``` java
IDatabaseConfig config = new DatabaseConfig.Builder()
                .dataSource("<url>", "<username>", "<password>")
                .dialect(new MyCustomSQLDialect())
                .build();
IDatabase db = new Database(config);
```

# Automatic serialization / deserialization of properties

The project supports conversion of properties to and from text (only) columns via JPA converters.

## JSON serialization / deserialization
The [JsonAttributeConverter](https://github.com/rlemaigre/JMiniORM/blob/master/src/main/java/org/jminiorm/attributeconverter/JsonAttributeConverter.java) can read/write any Java object from/to a text database column. It is based on [Jackson](https://github.com/FasterXML/jackson) (see for example [here](https://www.mkyong.com/java/jackson-2-convert-java-object-to-from-json/) to get started). It is invaluably useful to store complex data structures to a database without having to setup many tables and create complex mapping logic.

Suppose Users have a list of roles and a role is an instance of the Role class (which can be arbitrarily complex).

``` java
@Table(name = "users")
public class User {
    // ...other properties...
    private List<Role> roles;
}

public class Role {
    // ... properties, lists, maps, sub objects,...
}
```

To enable serialization of Roles to JSON, you must first define a converter class, like so :

``` java
public class RolesJsonConverter extends JsonAttributeConverter<List<Role>> {
    // Nothing to do here. The class is simply declared to capture the generic type
    // "List<Role>" in a way that is available at runtime for the JsonAttributeConverter
    // parent class.
}
```

Then assign the converter to the roles property :

``` java
@Table(...)
public class User {
    @Convert(converter = RolesJsonConverter.class)
    private List<Role> roles;
}
```
























