

mondrian-dialect
===================

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

What's happening
----------------

The dialect is registered via the Java Services API. There is a text file in the META-INF/services folder, mondrian.spi.Dialect. This file contians the class name of our example dialect. When this Jar is read by the JVM, the dialect will be added to the known dialects of Mondrian.

As a general rule of thumb, we recommend creating your dialect as a subclass of mondrian.spi.impl.JdbcDialectImpl. From there, you override only what's needed and what makes your dialect different from the usual SQL databases.

Being a subclass of JdbcDialectImpl, you will also need to override the static FACTORY field, like so.

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
