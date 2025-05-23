package sfedu.net.formator.application.security.rest

import arrow.core.Either.Companion.zipOrAccumulate
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.application.security.JwtTokenProvider
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.FullName
import sfedu.net.formator.domain.Role
import sfedu.net.formator.domain.Username
import sfedu.net.formator.persistence.UserRepository
import sfedu.net.formator.rest.toUserErrorResponse
import sfedu.net.formator.rest.validation.validated
import sfedu.net.formator.usecase.CreateUserUseCase
import sfedu.net.formator.usecase.UserUseCaseError
import sfedu.net.formator.util.restBusinessError
import sfedu.net.formator.util.toInvalidParamsBadRequest

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val createUserUseCase: CreateUserUseCase,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        return zipOrAccumulate(
            Username.validated(RegisterRequest::username.name, request.username),
            Email.validated(RegisterRequest::email.name, request.email),
            FullName.validated(RegisterRequest::fullName.name, request.fullName),
        ) { username, email, fullName ->
            val encodedPassword = passwordEncoder.encode(request.password)
            createUserUseCase(
                username = username,
                email = email,
                fullName = fullName,
                password = encodedPassword,
                role = Role.valueOf(request.role.uppercase())
            )
        }.fold(
            ifLeft = { it.toInvalidParamsBadRequest() },
            ifRight = { result ->
                result.fold(
                    ifRight = { user ->
                        val token = jwtTokenProvider.generateToken(user.email.value, user.role)
                        ResponseEntity.ok(AuthResponse(token))
                    },
                    ifLeft = { it.toUserErrorResponse() }
                )
            }
        )
    }
}

enum class CreateUserErrorType {
    USER_ALREADY_EXISTS,
    UNEXPECTED
}

data class CreateUserErrorResponse(val type: CreateUserErrorType, val message: String)

fun UserUseCaseError.toUserErrorResponse(): ResponseEntity<*> = when (this) {
    is UserUseCaseError.UserAlreadyExists ->
        restBusinessError(
            CreateUserErrorResponse(
                CreateUserErrorType.USER_ALREADY_EXISTS,
                "User with email ${this.email} already exists"
            )
        )

    is UserUseCaseError.Unexpected ->
        restBusinessError(
            CreateUserErrorResponse(
                CreateUserErrorType.UNEXPECTED,
                "Unexpected error: ${this.reason}"
            )
        )

    else -> {
        restBusinessError(
            CreateUserErrorResponse(
                CreateUserErrorType.UNEXPECTED,
                "Unexpected error"
            )
        )
    }
}