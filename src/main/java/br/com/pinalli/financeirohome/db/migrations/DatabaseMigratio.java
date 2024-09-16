package br.com.pinalli.financeirohome.db.migrations;

import org.flywaydb.core.Flyway;

 class DatabaseMigration {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/db_financeiro_home";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "admin";

    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();
    }
}