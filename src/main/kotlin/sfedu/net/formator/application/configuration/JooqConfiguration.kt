package sfedu.net.formator.application.configuration

import org.jooq.DSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import sfedu.net.formator.generated.tables.daos.TasksDao
import sfedu.net.formator.generated.tables.daos.UsersDao

@Configuration
class StorageConfiguration {

    @Bean
    fun carDao(dslContext: DSLContext) = UsersDao(dslContext.configuration())

    @Bean
    fun taskDao(dslContext: DSLContext) = TasksDao(dslContext.configuration())
}