package sfedu.net.formator.persistence.impl

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.User
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.generated.tables.Users.USERS
import sfedu.net.formator.generated.tables.daos.UsersDao
import sfedu.net.formator.persistence.UserRepository
import sfedu.net.formator.persistence.mappers.toDomain
import sfedu.net.formator.persistence.mappers.toEntity

@Component
class UserRepositoryImpl(
    private val dslContext: DSLContext,
    private val userDao: UsersDao
) : UserRepository {
    override fun save(user: User): User {
        val userEntity = user.toEntity()
        dslContext.insertInto(USERS)
            .set(USERS.ID, userEntity.id)
            .set(USERS.USERNAME, userEntity.username)
            .set(USERS.EMAIL, userEntity.email)
            .set(USERS.FULL_NAME, userEntity.fullName)
            .set(USERS.ROLE, userEntity.role)
            .set(USERS.CREATED_AT, userEntity.createdAt)
            .execute()
        return user
    }

    override fun findById(id: UserId): User? {
        val user = userDao.fetchOptionalById(id.value)
        return if (user.isPresent) {
            user.get().toDomain()
        } else {
            null
        }
    }

    override fun findAll(): List<User> {
        return userDao.findAll().map { it.toDomain() }
    }

    override fun deleteById(id: UserId) {
        TODO("Not yet implemented")
    }

    override fun update(user: User): User {
        TODO("Not yet implemented")
    }

    override fun userExists(email: Email): Boolean = userDao.fetchByEmail(email.value).isNotEmpty()

}