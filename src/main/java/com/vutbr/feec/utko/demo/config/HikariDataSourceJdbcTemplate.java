package com.vutbr.feec.utko.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * It is preferable to use DataSource over DriverManager because it maintains the connection pool, so you can reusing the same connections instead of the need to get new one each time.
 * <p>
 * Hikari Benchmark: https://github.com/brettwooldridge/HikariCP-benchmark
 * </p>
 *
 * @author Pavel Seda
 */
public class HikariDataSourceJdbcTemplate {

    private static HikariConfig config = new HikariConfig();
    private static volatile HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mqtt2godemo");
        config.setUsername("debian-sys-maint");
        config.setPassword("UOD9Ki9GsPPBehaE");
//        config.setUsername("root");
//        config.setPassword("");
        ds = new HikariDataSource(config);
    }

    private HikariDataSourceJdbcTemplate() {
    }

    public static synchronized DataSource getDataSource() throws SQLException {
        return ds;
    }

}
