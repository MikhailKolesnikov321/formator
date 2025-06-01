package sfedu.net.formator.util

import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.postgresql.ds.PGSimpleDataSource

fun runLiquibaseMigrations() {
    val changelogFile = "db.changelog/main.xml"
    val dbUrl = "jdbc:postgresql://localhost:5432/formator"
    val username = "postgres"
    val password = "postgres"

    val dataSource = PGSimpleDataSource().apply {
        setURL(dbUrl)
        user = username
        this.password = password
    }

    dataSource.connection.use { connection ->
        val database = DatabaseFactory
            .getInstance()
            .findCorrectDatabaseImplementation(JdbcConnection(connection))

        val liquibase = Liquibase(
            changelogFile,
            ClassLoaderResourceAccessor(),
            database
        )
        liquibase.update("")
        println("Liquibase migrations completed!")
    }
}