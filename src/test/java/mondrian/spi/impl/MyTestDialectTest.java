package mondrian.spi.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Assert;
import org.junit.Test;

public class MyTestDialectTest {

  private class MyException extends RuntimeException {
    private static final long serialVersionUID = -6873660959903586124L;
  };

  @Test
  public void testDialectAccept() throws Exception {

    try {

      // Make our dialect throw an exception when the accepts(...) method
      // is called. This means that our JAR was picked up by the
      // Java Services API.
      MyTestDialect.acceptHook = new Runnable() {
        public void run() {
          throw new MyException();
        }
      };

      DriverManager.getConnection(
          "jdbc:mondrian:Catalog=test-files/FoodMart.xml;"
          + "Jdbc=\"jdbc:hsqldb:mem:.\";"
          + "JdbcUser=sa;"
          + "JdbcPassword=" );

      Assert.fail( "Should have thrown MyException. Dialect was not loaded." );

    } catch ( MyException e ) {
      // all good.
    } finally {
      MyTestDialect.acceptHook = null;
    }
  }

  @Test
  public void testDialectUsage() throws Exception {

    // Make our dialect throw an exception when Mondrian asks it to
    // quote a column name. That means that at this point, the dialect
    // was recognized and accepted and used.
    MyTestDialect.quoteHook = new Runnable() {
      public void run() {
        throw new MyException();
      }
    };

    try (
      Connection conn = DriverManager.getConnection(
        "jdbc:mondrian:Catalog=test-files/FoodMart.xml;"
        + "Jdbc=jdbc:hsqldb:mem:.;" ); ) {

      Assert.fail( "Should have thrown MyException. Dialect was not loaded." );

    } catch ( Throwable e ) {
      if ( e instanceof MyException ) {
        // all good.
        return;
      }
      while ( e != null ) {
        e = e.getCause();
        if ( e instanceof MyException ) {
          // all good.
          return;
        }
      }
      Assert.fail( "Failed." );
    } finally {
      MyTestDialect.quoteHook = null;
    }
  }
}
