package sfedu.net.formator.rest

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.application.security.CurrentUserProvider
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.rest.dto.CreateReportRequest
import sfedu.net.formator.usecase.CreateReportUseCase
import sfedu.net.formator.usecase.CreateReportUseCaseError
import sfedu.net.formator.util.restBusinessError

@RestController
class CreateReportEndpoint(
    private val useCase: CreateReportUseCase,
    private val currentUserProvider: CurrentUserProvider
) {

    @Operation(summary = "Create report endpoint", tags = ["Task"])
    @PostMapping("/api/v1/student/report")
    operator fun invoke(@RequestBody request: CreateReportRequest): ResponseEntity<*> {
        val supervisionId = currentUserProvider.getCurrentUserId()
        return useCase(UserId(request.id)).fold(
            ifLeft = { it.toErrorResponse() },
            ifRight = {
                ResponseEntity.ok()
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=${it.first}"
                    )
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(it.second)
            },
        )
    }
}

enum class CreateReportErrorType {
    STUDENT_NOT_FOUND,
    TASK_NOT_FOUND
}

data class CreateReportErrorResponse(
    val type: CreateReportErrorType,
    val message: String
)


fun CreateReportUseCaseError.toErrorResponse(): ResponseEntity<*> = when (this) {
    is CreateReportUseCaseError.StudentNotFound ->
        restBusinessError(
            CreateReportErrorResponse(
                CreateReportErrorType.STUDENT_NOT_FOUND,
                message
            )
        )

    is CreateReportUseCaseError.TasksNotFound ->
        restBusinessError(
            CreateReportErrorResponse(
                CreateReportErrorType.TASK_NOT_FOUND,
                message
            )
        )
}