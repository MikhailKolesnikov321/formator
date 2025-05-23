package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.User
import sfedu.net.formator.persistence.UserRepository

@Component
class AuthUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    operator fun invoke(email: Email, rawPassword: String): Either<UserAuthError, User> {
        val user = userRepository.findByEmail(email.value)
            ?: return UserAuthError.UserNotFound(email).left()

        return if (passwordEncoder.matches(rawPassword, user.password)) {
            user.right()
        } else {
            UserAuthError.InvalidPassword.left()
        }
    }
}

sealed interface UserAuthError {
    data class UserNotFound(val email: Email) : UserAuthError
    data object InvalidPassword : UserAuthError
    data class Unexpected(val reason: String) : UserAuthError
}