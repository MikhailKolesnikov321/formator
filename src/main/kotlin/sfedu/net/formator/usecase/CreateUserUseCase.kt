package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.FullName
import sfedu.net.formator.domain.Organization
import sfedu.net.formator.domain.Role
import sfedu.net.formator.domain.User
import sfedu.net.formator.persistence.UserRepository

@Component
class CreateUserUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        username: Organization,
        email: Email,
        fullName: FullName,
        password: String,
        role: Role
    ): Either<UserUseCaseError, User> {
        return if (userRepository.userExists(email.value)) {
            UserUseCaseError.UserAlreadyExists(email.value).left()
        } else {
            User.create(username, email, password, fullName, role).let { user ->
                userRepository.save(user).right()
            }
        }
    }
}

sealed class UserUseCaseError {
    data class UserAlreadyExists(val email: String) : UserUseCaseError()
    data class Unexpected(val reason: String) : UserUseCaseError()
}
