package sfedu.net.formator.domain

import java.time.OffsetDateTime

data class User(
    var id: UserId,
    var organization: Organization,
    var email: Email,
    var fullName: FullName,
    var password: String,
    var role: Role,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        fun create(
            organization: Organization,
            email: Email,
            password: String,
            fullName: FullName,
            role: Role
        ): User {
            return User(
                id = UserId.generate(),
                organization = organization,
                email = email,
                password = password,
                fullName = fullName,
                role = role
            )
        }
    }
}