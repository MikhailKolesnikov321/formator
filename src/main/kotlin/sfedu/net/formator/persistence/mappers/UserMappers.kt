package sfedu.net.formator.persistence.mappers

import arrow.core.getOrElse
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.FullName
import sfedu.net.formator.domain.Role
import sfedu.net.formator.domain.User
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.domain.Username
import sfedu.net.formator.generated.tables.pojos.Users

fun Users.toDomain(): User {
    return User(
        id = UserId.from(id),
        username = Username.from(username).getOrElse {
            error("Can't create username from $username")
        },
        email = Email.from(email).getOrElse {
            error("Can't create email from $email")
        },
        fullName = FullName.aggregateName(fullName).getOrElse {
            error("Can't create fullName from $fullName")
        },
        role = Role.valueOf(role),
        createdAt = createdAt,
        password = password
    )
}

fun User.toEntity(): Users {
    val user = Users()
    user.id = this.id.value
    user.createdAt = this.createdAt
    user.email = this.email.value
    user.fullName = this.fullName.getFullName()
    user.username = this.username.value
    user.role = this.role.name
    user.password = this.password
    return user
}
