package com.bookshop.admin.configuration

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class DatabaseConfiguration(val dataSource: DataSource) {
    @Bean
    fun databaseConfig(): DatabaseConfig {
        return DatabaseConfig { useNestedTransactions = true }
    }

    @Bean
    fun database(): Database {
        return Database.connect(dataSource)
    }
}