package sfedu.net.formator.domain

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import sfedu.net.formator.util.ValidationError
import java.util.UUID


@JvmInline
value class UserId(val value: UUID) {
    companion object {
        fun generate() = UserId(UUID.randomUUID())
        fun from(id: UUID) = UserId(id)
    }
}

data class Username private constructor(val value: String) {
    companion object {
        fun from(input: String): Either<UserError, Username> {
            return when {
                input.isBlank() -> UserError.Empty.left()
                input.length < 3 -> UserError.TooShort(3).left()
                input.length > 50 -> UserError.TooLong(50).left()
                !input.matches(Regex("^[a-zA-Z0-9_.-]+\$")) -> UserError.InvalidCharacters.left()
                else -> Username(input).right()
            }
        }
    }
}

sealed interface UserError : ValidationError {
    data object Empty : UserError {
        override val message = "Username cannot be empty"
    }
    data class TooShort(val min: Int) : UserError {
        override val message = "Username must be at least $min characters"
    }
    data class TooLong(val max: Int) : UserError {
        override val message = "Username cannot exceed $max characters"
    }
    data object InvalidCharacters : UserError {
        override val message = "Username contains invalid characters"
    }
}

data class Email private constructor(val value: String) {
    companion object {
        fun from(input: String): Either<EmailError, Email> {
            return when {
                input.isBlank() -> EmailError.Empty.left()
                !input.matches(Regex("^[\\w.-]+@[\\w.-]+\\.\\w+\$")) -> EmailError.InvalidFormat.left()
                else -> Email(input).right()
            }
        }
    }
}

sealed interface EmailError : ValidationError {
    data object Empty : EmailError {
        override val message = "Email cannot be empty"
    }
    data object InvalidFormat : EmailError {
        override val message = "Invalid email format"
    }
}

data class FullName private constructor(
    val firstName: String,
    val lastName: String
) {
    companion object {
        fun from(first: String, last: String): Either<NameError, FullName> {
            return validateNamePart(first, "First name")
                .flatMap { firstName ->
                    validateNamePart(last, "Last name")
                        .map { lastName -> FullName(firstName, lastName) }
                }
        }

        private fun validateNamePart(value: String, field: String): Either<NameError, String> {
            return when {
                value.isBlank() -> NameError.Empty(field).left()
                value.length > 100 -> NameError.TooLong(field, 100).left()
                !value.matches(Regex("^[\\p{L} '-]+\$")) -> NameError.InvalidCharacters(field).left()
                else -> value.right()
            }
        }

        fun aggregateName(full: String): Either<NameError, FullName> {
            val parts = full.trim().split(Regex("\\s+"))
            if (parts.size < 2) {
                return NameError.InvalidCharacters("Expected format: 'First Last' or 'Last First'").left()
            }

            val first = parts[0]
            val last = parts.subList(1, parts.size).joinToString(" ")

            return from(first, last)
        }
    }

    fun getFullName(): String = "$lastName $firstName"
}

sealed interface NameError : ValidationError {
    data class Empty(val field: String) : NameError {
        override val message = "$field cannot be empty"
    }
    data class TooLong(val field: String, val max: Int) : NameError {
        override val message = "$field cannot exceed $max characters"
    }
    data class InvalidCharacters(val field: String) : NameError {
        override val message = "$field contains invalid characters"
    }
}

enum class Role {
    STUDENT,
    UNIVERSITY_SUPERVISOR,
    ORGANIZATION_SUPERVISOR,
    ADMIN
}