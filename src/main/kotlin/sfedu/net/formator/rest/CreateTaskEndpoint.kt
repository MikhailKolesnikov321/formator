package sfedu.net.formator.rest

import arrow.core.Either.Companion.zipOrAccumulate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.domain.Description
import sfedu.net.formator.domain.Title
import sfedu.net.formator.rest.dto.CreateTaskRequest
import sfedu.net.formator.rest.dto.CreateTaskResponse
import sfedu.net.formator.rest.validation.validateDateInterval
import sfedu.net.formator.rest.validation.validated
import sfedu.net.formator.usecase.CreateTaskUseCase
import sfedu.net.formator.usecase.TaskUseCaseError
import sfedu.net.formator.util.restBusinessError
import sfedu.net.formator.util.toInvalidParamsBadRequest

@RestController
class CreateTaskEndpoint(
    private val useCase: CreateTaskUseCase
) {

    @Operation(summary = "Create task endpoint", tags = ["Task"])
    @PostMapping("/api/v1/organization/task/create")
    operator fun invoke(@RequestBody request: CreateTaskRequest): ResponseEntity<*> {
        return zipOrAccumulate(
            Title.validated(request.title),
            Description.validated(request.description),
            validateDateInterval(request.startAt, request.endAt)
        ) { title, description, date ->
            useCase(title, description, date.first, date.second)
        }.fold(
            ifLeft = { it.toInvalidParamsBadRequest() },
            ifRight = { result ->
                result.fold(
                    ifLeft = { it.toTaskErrorResponse() },
                    ifRight = { task ->
                        ResponseEntity.ok(
                            CreateTaskResponse(
                                id = task.id.uuidValue()
                            )
                        )
                    }
                )
            }
        )
    }
}

enum class TaskErrorType {
    INVALID_DATE_RANGE
}

data class TaskErrorResponse(
    val type: TaskErrorType,
    val message: String
)

fun TaskUseCaseError.toTaskErrorResponse(): ResponseEntity<*> = when (this) {
    is TaskUseCaseError.InvalidDateRange ->
        restBusinessError(
            TaskErrorResponse(
                TaskErrorType.INVALID_DATE_RANGE,
                reason
            )
        )
}