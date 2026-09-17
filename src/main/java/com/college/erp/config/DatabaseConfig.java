package com.college.erp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();

        String mysqlHost = System.getenv("MYSQLHOST");
        String dbHost = System.getenv("DB_HOST");
        String mysqlUrl = System.getenv("MYSQL_URL");
        String dbUrl = System.getenv("DB_URL");

        String host = (mysqlHost != null && !mysqlHost.isEmpty()) ? mysqlHost : dbHost;
        String port = System.getenv("MYSQLPORT") != null ? System.getenv("MYSQLPORT") : System.getenv("DB_PORT");
        String dbName = System.getenv("MYSQLDATABASE") != null ? System.getenv("MYSQLDATABASE") : System.getenv("DB_NAME");
        String user = System.getenv("MYSQLUSER") != null ? System.getenv("MYSQLUSER") : System.getenv("DB_USERNAME");
        String pass = System.getenv("MYSQLPASSWORD") != null ? System.getenv("MYSQLPASSWORD") : System.getenv("DB_PASSWORD");

        if (dbName == null || dbName.isEmpty()) dbName = "college_erp";

        if (host != null && !host.isEmpty()) {
            if (port == null || port.isEmpty()) port = "3306";
            if (user == null || user.isEmpty()) user = "root";
            if (pass == null) pass = "";

            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            log.info("Configuring Cloud MySQL DataSource: {}", jdbcUrl);
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setJdbcUrl(jdbcUrl);
            ds.setUsername(user);
            ds.setPassword(pass);
        } else if (mysqlUrl != null && mysqlUrl.startsWith("mysql://")) {
            String jdbcUrl = mysqlUrl.replace("mysql://", "jdbc:mysql://");
            if (!jdbcUrl.contains("?")) {
                jdbcUrl += "?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            }
            log.info("Configuring Cloud MySQL DataSource from MYSQL_URL: {}", jdbcUrl);
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setJdbcUrl(jdbcUrl);
        } else if (dbUrl != null && dbUrl.startsWith("jdbc:mysql://")) {
            log.info("Configuring DataSource from DB_URL: {}", dbUrl);
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setJdbcUrl(dbUrl);
            if (user != null) ds.setUsername(user);
            if (pass != null) ds.setPassword(pass);
        } else {
            log.info("No Cloud MySQL connection detected. Initializing embedded H2 Database for instant zero-config startup.");
            ds.setDriverClassName("org.h2.Driver");
            ds.setJdbcUrl("jdbc:h2:mem:college_erp;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL");
            ds.setUsername("sa");
            ds.setPassword("");
        }

        return ds;
    }
}
