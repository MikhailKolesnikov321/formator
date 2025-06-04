package sfedu.net.formator.application.security.rest

import arrow.core.Either.Companion.zipOrAccumulate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.application.security.JwtTokenProvider
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.Password
import sfedu.net.formator.rest.validation.validated
import sfedu.net.formator.usecase.AuthUserUseCase
import sfedu.net.formator.usecase.UserAuthError
import sfedu.net.formator.util.restBusinessError
import sfedu.net.formator.util.toInvalidParamsBadRequest

@RestController
class AuthEndpoint(
    private val useCase: AuthUserUseCase,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Operation(summary = "Login endpoint", tags = ["Authorization"])
    @PostMapping("/api/v1/auth/login")
    operator fun invoke(@RequestBody request: LoginRequest): ResponseEntity<*> {
        return zipOrAccumulate(
            Email.validated(request.email),
            Password.validated(request.password)
        ) { email, password ->
            useCase(email, password.value)
        }.fold(
            ifLeft = { it.toInvalidParamsBadRequest() },
            ifRight = { result ->
                result.fold(
                    ifRight = { user ->
                        val token = jwtTokenProvider.generateToken(user.email.value, user.role)
                        ResponseEntity.ok(
                            LoginResponse(
                                user.id.uuidValue(),
                                token
                            )
                        )
                    },
                    ifLeft = { it.toAuthErrorResponse() }
                )
            }
        )
    }
}

enum class AuthErrorType {
    USER_NOT_FOUND,
    INVALID_PASSWORD,
    UNEXPECTED
}

data class UserAuthErrorResponse(
    val type: AuthErrorType,
    val message: String
)

fun UserAuthError.toAuthErrorResponse(): ResponseEntity<*> = when (this) {
    is UserAuthError.UserNotFound ->
        restBusinessError(
            UserAuthErrorResponse(
                AuthErrorType.USER_NOT_FOUND,
                "User with email ${email.value} not found"
            )
        )

    is UserAuthError.InvalidPassword ->
        restBusinessError(
            UserAuthErrorResponse(
                AuthErrorType.INVALID_PASSWORD,
                "Invalid password"
            )
        )

    is UserAuthError.Unexpected ->
        restBusinessError(
            UserAuthErrorResponse(
                AuthErrorType.UNEXPECTED,
                "Unexpected error: $reason"
            )
        )
}
