package org.daCoffee.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseConnectionChecker implements ApplicationRunner {

  private final DataSource dataSource;

  @Override
  public void run(ApplicationArguments args) {
    try (Connection conn = dataSource.getConnection()) {
      log.info("Database connection successful: {}", conn);
    } catch (SQLException e) {
      log.error("Failed to connect to database: ", e);
    }
  }
}