package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.persistence.TaskRepository
import sfedu.net.formator.persistence.UserRepository
import java.util.*

@Component
class AssignTasksToStudentUseCase(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(
        studentId: UUID,
        taskIds: List<UUID>
    ): Either<AssignTasksError, Unit> {
        if (userRepository.findById(UserId(studentId)) == null) {
            return AssignTasksError.StudentNotFound("User with id: $studentId not found").left()
        }
    }
}

sealed class AssignTasksError {
    data class StudentNotFound(val message: String) : AssignTasksError()
    data class TasksNotFound(val message: String) : AssignTasksError()
}