package com.example.myapp.auth

import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Configuration

object Members : LongIdTable("member") {
    val secret = varchar("secret", 200)
    val username = varchar("username", length = 100)
    val email = varchar("email", 200)
    val mname = varchar("mname", 100)
}

@Configuration
class AuthTableSetup(private val database: Database) {
    @PostConstruct
    fun migrateSchema() {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(Members)
        }
    }
}
