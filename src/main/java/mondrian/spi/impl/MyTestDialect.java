package mondrian.spi.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * This is an example of a dialect for Mondrian. The important part
 * to register properly is to have a descriptor in the JAR for this
 * dialect service. (see mondrian-dialect.jar:/META-INF/services/mondrian.spi.Dialect)
 *
 * <p>Being a subclass of {@link JdbcDialectImpl}, we also need to override
 * the {@link #FACTORY} static field.
 *
 * <p>Please also note that this class contains some code for testing purposes,
 * and this code was only added to simplify testing. In production ready code,
 * you should remove such artifacts.
 */
public class MyTestDialect extends HsqldbDialect {

  private static final Logger LOGGER =
      Logger.getLogger( MyTestDialect.class.getCanonicalName() );

  // This only exists so that we can run some special tests.
  // Do not use such a thing in production code.
  static Runnable acceptHook = null;
  static Runnable quoteHook = null;

  static {
    LOGGER.info( "MyTestDialect loaded in classloader." );
  }

  //
  public static final JdbcDialectFactory FACTORY =
      new JdbcDialectFactory(
        MyTestDialect.class,
        DatabaseProduct.UNKNOWN )
  {

    protected boolean acceptsConnection( Connection connection ) {

      // This only exists so that we can run some special tests.
      // Do not use such a thing in production code.
      if ( acceptHook != null ) {
        acceptHook.run();
      }

      // This is where you would add some logic to determine if your dialect
      // should handle this sql connection.
      // You can use
      //    java.sql.Connection..getMetaData().getDatabaseProductName()

      LOGGER.info( "Accepting connection arbitrarily." );
      return true;

    }
  };

  @Override
  public boolean allowsDialectSharing() {
    return false;
  }

  @Override
  public void quoteIdentifier( String val, StringBuilder buf ) {
    // This only exists so that we can run some special tests.
    // Do not use such a thing in production code.
    if ( quoteHook != null ) {
      quoteHook.run();
    }
    super.quoteIdentifier( val, buf );
  }

  public MyTestDialect( Connection connection ) throws SQLException {
    super( connection );
  }
}
