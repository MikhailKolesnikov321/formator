package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.Answer
import sfedu.net.formator.domain.TaskId
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.persistence.TaskRepository
import sfedu.net.formator.persistence.UserRepository

@Component
class AddAnswerUseCase(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(
        studentId: UserId,
        taskId: TaskId,
        answer: String,
    ): Either<AddAnswerUseCaseError, Unit> {
        userRepository.findById(studentId) ?: return AddAnswerUseCaseError.StudentNotFound("User with id: ${studentId.value} not found").left()
        taskRepository.findById(taskId) ?: return AddAnswerUseCaseError.TasksNotFound("Task with id: ${taskId.value} not found").left()

        taskRepository.saveTaskAndUser(taskId, studentId, null, answer)
        return Unit.right()
    }
}

sealed class AddAnswerUseCaseError {
    data class StudentNotFound(val message: String) : AddAnswerUseCaseError()
    data class TasksNotFound(val message: String) : AddAnswerUseCaseError()
}