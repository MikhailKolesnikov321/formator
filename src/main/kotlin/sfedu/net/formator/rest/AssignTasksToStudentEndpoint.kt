package sfedu.net.formator.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.rest.dto.AssignTasksRequest
import sfedu.net.formator.usecase.AssignTasksToStudentUseCase
import sfedu.net.formator.usecase.AssignTasksUseCaseError
import sfedu.net.formator.util.restBusinessError

@RestController
class AssignTasksToStudentEndpoint(
    private val useCase: AssignTasksToStudentUseCase
) {
    @PostMapping("/api/v1/organization/task/assign")
    operator fun invoke(@RequestBody request: AssignTasksRequest): ResponseEntity<*> {
        return useCase(request.studentId, request.taskIds).fold(
            ifLeft = { it.toTaskErrorResponse() },
            ifRight = {
                ResponseEntity.ok("successfully added")
            }
        )
    }
}

enum class AssignTasksErrorType {
    STUDENT_NOT_FOUND,
    TASK_NOT_FOUND
}

data class AssignTasksErrorResponse(
    val type: AssignTasksErrorType,
    val message: String
)


fun AssignTasksUseCaseError.toTaskErrorResponse(): ResponseEntity<*> = when (this) {
    is AssignTasksUseCaseError.StudentNotFound ->
        restBusinessError(
            AssignTasksErrorResponse(
                AssignTasksErrorType.STUDENT_NOT_FOUND,
                message
            )
        )

    is AssignTasksUseCaseError.TasksNotFound ->
        restBusinessError(
            AssignTasksErrorResponse(
                AssignTasksErrorType.TASK_NOT_FOUND,
                message
            )
        )
}