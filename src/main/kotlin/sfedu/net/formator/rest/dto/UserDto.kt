package sfedu.net.formator.rest.dto

import java.time.OffsetDateTime
import java.util.*

data class CreateUserRequest(
    val username: String,
    val email: String,
    val fullName: String,
    val password: String,
    val role: String
)

data class CreateUserResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val fullName: String,
    val role: String,
    val createdAt: OffsetDateTime
)