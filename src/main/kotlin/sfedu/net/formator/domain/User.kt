package sfedu.net.formator.domain

import java.time.OffsetDateTime

data class User(
    var id: UserId,
    var username: Username,
    var email: Email,
    var fullName: FullName,
    var password: String,
    var role: Role,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        fun create(
            username: Username,
            email: Email,
            password: String,
            fullName: FullName,
            role: Role
        ): User {
            return User(
                id = UserId.generate(),
                username = username,
                email = email,
                password = password,
                fullName = fullName,
                role = role
            )
        }
    }
}