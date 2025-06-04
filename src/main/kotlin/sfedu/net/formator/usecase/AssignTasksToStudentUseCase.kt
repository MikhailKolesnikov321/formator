package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.TaskId
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
        taskIds: Set<UUID>
    ): Either<AssignTasksUseCaseError, Unit> {
        if (userRepository.findById(UserId(studentId)) == null) {
            return AssignTasksUseCaseError.StudentNotFound("User with id: $studentId not found").left()
        }

        taskIds.forEach {
            if (taskRepository.findById(TaskId(it)) == null) {
                return AssignTasksUseCaseError.TasksNotFound("Task with id: $it not found").left()
            }
        }
        val taskToOrder: Map<TaskId, Int> = taskIds
            .map { TaskId(it) }
            .withIndex()
            .associate { (index, taskId) -> taskId to index + 1 }
        taskToOrder.forEach {
            taskRepository.saveTaskAndUser(it.key, UserId(studentId), it.value, null)
        }
        return Unit.right()
    }
}

sealed class AssignTasksUseCaseError {
    data class StudentNotFound(val message: String) : AssignTasksUseCaseError()
    data class TasksNotFound(val message: String) : AssignTasksUseCaseError()
}