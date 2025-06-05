package sfedu.net.formator.rest

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.rest.dto.CreateReportRequest
import sfedu.net.formator.usecase.GetStudentsForOrganizationUseCase
import sfedu.net.formator.usecase.GetStudentsForOrganizationUseCaseError
import sfedu.net.formator.util.restBusinessError
import java.util.UUID

@RestController
class GetStudentsForOrganizationEndpoint(
    private val useCase: GetStudentsForOrganizationUseCase
) {

    @GetMapping("/api/v1/organization/students")
    operator fun invoke(authentication: Authentication): ResponseEntity<*> {
        val supervisorId = authentication.principal.toString()
        return useCase(UserId(UUID.fromString(supervisorId))).fold(
            ifLeft = { it.toErrorResponse() },
            ifRight = { ResponseEntity.ok(it) },
        )
    }
}

enum class GetStudentsForOrganizationErrorType {
    ORGANIZATION_SUPERVISOR_NOT_FOUND
}

data class GetStudentsForOrganizationResponse(
    val type: GetStudentsForOrganizationErrorType,
    val message: String
)


fun GetStudentsForOrganizationUseCaseError.toErrorResponse(): ResponseEntity<*> = when (this) {
    is GetStudentsForOrganizationUseCaseError.SupervisorNotFound ->
        restBusinessError(
            GetStudentsForOrganizationResponse(
                GetStudentsForOrganizationErrorType.ORGANIZATION_SUPERVISOR_NOT_FOUND,
                message
            )
        )
}