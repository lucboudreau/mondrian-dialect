

# mondrian-dialect

This project is a demo creating a dialect for Mondrian, the OLAP engine.

Playing with the code
-----------------

To test this project, you will need to clone this repository locally.

```
git clone https://github.com/lucboudreau/mondrian-dialect.git
```

Then you can run the code using Maven.

```
mvn test
```

You can also create a Jar package and use it as an example of a packaged dialect, ready for use by Mondrian.

```
mvn package
```

## What's happening

The dialect is registered via the Java Services API. There is a text file in the `META-INF/services` folder, `mondrian.spi.Dialect`. This file contians the class name of our example dialect. When this Jar is read by the JVM, the dialect will be added to the known dialects of Mondrian.

As a general rule of thumb, we recommend creating your dialect as a subclass of `mondrian.spi.impl.JdbcDialectImpl`. From there, you override only what's needed and what makes your dialect different from the usual SQL databases.

Being a subclass of `JdbcDialectImpl`, you will also need to override the static `FACTORY` field, like so.

```java
    public static final JdbcDialectFactory FACTORY =
        new JdbcDialectFactory(
            MyTestDialect.class,
            DatabaseProduct.UNKNOWN)
        {
            protected boolean acceptsConnection(Connection connection) {
                // This is where you can inspect the connection and
                // decide if this dialect is appropriate.
                return true;
            }
        };
```

## I have a dialect. Now what?

Once you have a working dialect, you can investigate how well it works. There are two options.

 - Use the `DialectTest` from Mondrian
 - Use the [Mondrian Test Compatibility Kit](https://github.com/pentaho/mondrian-tck) (TCK)

### Using the DialectTest from Mondrian

The dialect test is a JUnit test class which exists in the mondrian project. It will test your dialect and your JDBC source together. It tries the different methods of the dialect and will tell you if the expected behavior matches what the dialect says. 

So for example, if your dialect overrides `allowsCountDistinct()`, Mondrian will try to run a query using the distinct count aggregator. If your dialect said that your DB does not support this aggregator however, Mondrian will also try to run the query anyway and makes sure that it fails with the proper error codes. This is particularly useful when dealing with regressions in behavior between different backend versions.

To obtain and run the test, you will need to follow the instructions from the Mondrian project [here](http://htmlpreview.github.io/?https://github.com/pentaho/mondrian/blob/master/doc/developer.html).

### Using the TCK

The [Mondrian TCK](https://github.com/pentaho/mondrian-tck) offers a different perspective when testing your dialect. The TCK's aim is to validate that your DB and dialect have the required minimum functionality for Mondrian to operate. It will tell you if any of these features are not supported, or leveraged properly by Mondrian using your dialect.

 - Table joins (ANSI SQL 89 and 92 style)
 - JDBC Driver Thread safety and concurrency support
 - Predicates, simple and compounded
 - Grouping sets
 - JDBC Driver Table and Columns Metadata
 - Handling of `null` values and keys
 - Distinct aggregators
 - Row limiting

The TCK tests a lof of things, and there are always failures to be expected. It is important to validate the results carefully. Not all databases can support everything, and none so far have fully passed our tests. Some of the features are important, like proper support for table joins, but others are only helping to gain performance, like grouping sets.

To download and run the TCK, please refer to the README instructions [here](https://github.com/pentaho/mondrian-tck/blob/master/README.md).
