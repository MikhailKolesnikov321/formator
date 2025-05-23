package sfedu.net.formator.domain

import java.time.OffsetDateTime

class User(
    var id: UserId,
    var username: Username,
    var email: Email,
    var fullName: FullName,
    var role: Role,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        fun create(
            username: Username,
            email: Email,
            fullName: FullName,
            role: Role
        ): User {
             return User(
                id = UserId.generate(),
                username = username,
                email = email,
                fullName = fullName,
                role = role
            )
        }
    }
}