package sfedu.net.formator.rest

import arrow.core.Either.Companion.zipOrAccumulate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.FullName
import sfedu.net.formator.domain.Role
import sfedu.net.formator.domain.Username
import sfedu.net.formator.rest.dto.CreateUserRequest
import sfedu.net.formator.rest.dto.CreateUserResponse
import sfedu.net.formator.rest.validation.validated
import sfedu.net.formator.usecase.CreateUserUseCase
import sfedu.net.formator.usecase.UserUseCaseError
import sfedu.net.formator.util.restBusinessError
import sfedu.net.formator.util.toInvalidParamsBadRequest

//@RestController
//@RequestMapping("/api/v1")
//class CreateUserEndpoint(
//    private val useCase: CreateUserUseCase
//) {
//    @Operation(summary = "Create user", tags = ["User"])
//    @PostMapping("/user")
//    operator fun invoke(@RequestBody request: CreateUserRequest ): ResponseEntity<*> {
//        return zipOrAccumulate(
//            Username.validated(CreateUserRequest::username.name, request.username),
//            Email.validated(CreateUserRequest::email.name, request.email),
//            FullName.validated(CreateUserRequest::fullName.name, request.fullName),
//        ) { username, email, fullName ->
//            useCase(
//                username = username,
//                email = email,
//                fullName = fullName,
//                password = request.password,
//                role = Role.valueOf(request.role.uppercase())
//            )
//        }.fold(
//            ifLeft = { it.toInvalidParamsBadRequest() },
//            ifRight = { result ->
//                result.fold(
//                    ifRight = { user ->
//                        ResponseEntity.status(201).body(
//                            CreateUserResponse(
//                                user.id.value,
//                                user.username.value,
//                                user.email.value,
//                                user.fullName.getFullName(),
//                                user.role.name,
//                                user.createdAt
//                            )
//                        )
//                    },
//                    ifLeft = { it.toUserErrorResponse() }
//                )
//            }
//        )
//    }
//}
//
//enum class CreateUserErrorType {
//    USER_ALREADY_EXISTS,
//    UNEXPECTED
//}
//
//data class CreateUserErrorResponse(val type: CreateUserErrorType, val message: String)
//
//fun UserUseCaseError.toUserErrorResponse(): ResponseEntity<*> = when (this) {
//    is UserUseCaseError.UserAlreadyExists ->
//        restBusinessError(
//            CreateUserErrorResponse(
//                CreateUserErrorType.USER_ALREADY_EXISTS,
//                "User with email ${this.email} already exists"
//            )
//        )
//
//    is UserUseCaseError.Unexpected ->
//        restBusinessError(
//            CreateUserErrorResponse(
//                CreateUserErrorType.UNEXPECTED,
//                "Unexpected error: ${this.reason}"
//            )
//        )
//
//    else -> {
//        restBusinessError(
//            CreateUserErrorResponse(
//                CreateUserErrorType.UNEXPECTED,
//                "Unexpected error"
//            )
//        )
//    }
//}