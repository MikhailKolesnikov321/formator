package sfedu.net.formator.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sfedu.net.formator.domain.TaskId
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.rest.dto.AddAnswerRequest
import sfedu.net.formator.usecase.AddAnswerUseCase
import sfedu.net.formator.usecase.AddAnswerUseCaseError
import sfedu.net.formator.util.restBusinessError

@RestController
class AddAnswerEndpoint(
    private val useCase: AddAnswerUseCase
) {

    @PostMapping("/api/v1/organization/task/answer")
    operator fun invoke(@RequestBody request: AddAnswerRequest): ResponseEntity<*> {
        return useCase(
            UserId(request.studentId),
            TaskId(request.taskId),
            request.answer
        ).fold(
            ifLeft = { it.toTaskErrorResponse() },
            ifRight = { ResponseEntity.ok("Answer successful added") },
        )
    }
}

enum class AddAnswerErrorType {
    STUDENT_NOT_FOUND,
    TASK_NOT_FOUND
}

data class AddAnswerErrorResponse(
    val type: AddAnswerErrorType,
    val message: String
)


fun AddAnswerUseCaseError.toTaskErrorResponse(): ResponseEntity<*> = when (this) {
    is AddAnswerUseCaseError.StudentNotFound ->
        restBusinessError(
            AddAnswerErrorResponse(
                AddAnswerErrorType.STUDENT_NOT_FOUND,
                message
            )
        )

    is AddAnswerUseCaseError.TasksNotFound ->
        restBusinessError(
            AddAnswerErrorResponse(
                AddAnswerErrorType.TASK_NOT_FOUND,
                message
            )
        )
}