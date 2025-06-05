package sfedu.net.formator.persistence.impl

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.User
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.generated.tables.Users.USERS
import sfedu.net.formator.generated.tables.daos.UsersDao
import sfedu.net.formator.generated.tables.pojos.Users
import sfedu.net.formator.persistence.UserRepository
import sfedu.net.formator.persistence.mappers.toDomain
import sfedu.net.formator.persistence.mappers.toEntity
import java.util.*

@Component
class UserRepositoryImpl(
    private val dslContext: DSLContext,
    private val userDao: UsersDao
) : UserRepository {
    override fun save(user: User): User {
        val userEntity = user.toEntity()
        dslContext.transaction { ctx ->
            ctx.dsl()
                .insertInto(USERS)
                .set(USERS.ID, userEntity.id)
                .set(USERS.ORGANIZATION, userEntity.organization)
                .set(USERS.EMAIL, userEntity.email)
                .set(USERS.FULL_NAME, userEntity.fullName)
                .set(USERS.PASSWORD, userEntity.password)
                .set(USERS.ROLE, userEntity.role)
                .set(USERS.CREATED_AT, userEntity.createdAt)
                .execute()
        }

        return user
    }

    override fun findById(id: UserId): User? {
        return userDao.fetchOptionalById(id.value).map { it.toDomain() }.orElse(null)
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

    override fun userExists(email: String): Boolean = userDao.fetchByEmail(email).isNotEmpty()

    override fun findByEmail(email: String): User? {
        return dslContext.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .fetchOneInto(Users::class.java)?.toDomain()
    }

    override fun findStudentsBySupervisor(supervisorId: UserId): List<User> {
        val organization = dslContext
            .select(USERS.ORGANIZATION)
            .from(USERS)
            .where(USERS.ID.eq(supervisorId.uuidValue()))
            .fetchOne(USERS.ORGANIZATION)

        return dslContext
            .selectFrom(USERS)
            .where(
                USERS.ORGANIZATION.eq(organization)
                    .and(USERS.ROLE.eq("STUDENT"))
            )
            .fetchInto(Users::class.java)
            .map { it.toDomain() }
    }
}