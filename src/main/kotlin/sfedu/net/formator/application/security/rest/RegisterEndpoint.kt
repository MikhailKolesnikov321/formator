package sfedu.net.formator.application.security.rest

import arrow.core.Either.Companion.zipOrAccumulate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.application.security.JwtTokenProvider
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.FullName
import sfedu.net.formator.domain.Organization
import sfedu.net.formator.domain.Password
import sfedu.net.formator.domain.Role
import sfedu.net.formator.rest.validation.validated
import sfedu.net.formator.usecase.CreateUserUseCase
import sfedu.net.formator.usecase.UserUseCaseError
import sfedu.net.formator.util.restBusinessError
import sfedu.net.formator.util.toInvalidParamsBadRequest

@RestController
class RegisterController(
    private val createUserUseCase: CreateUserUseCase,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Operation(summary = "Register endpoint", tags = ["Authorization"])
    @PostMapping("/api/v1/auth/register")
    operator fun invoke(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        return zipOrAccumulate(
            Organization.validated(request.organization),
            Email.validated(request.email),
            FullName.validated(request.fullName),
            Password.validated(request.password)
        ) { username, email, fullName, password ->
            val encodedPassword = passwordEncoder.encode(password.value)
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
                        val token = jwtTokenProvider.generateToken(user.id.uuidValue().toString(), user.role)
                        ResponseEntity.ok(
                            RegisterResponse(
                                user.id.uuidValue(),
                                token
                            )
                        )
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