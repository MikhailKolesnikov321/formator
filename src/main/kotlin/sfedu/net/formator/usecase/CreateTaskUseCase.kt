package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.Description
import sfedu.net.formator.domain.Task
import sfedu.net.formator.domain.Title
import sfedu.net.formator.persistence.TaskRepository
import java.time.LocalDate

@Component
class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    operator fun invoke(
        title: Title,
        description: Description,
        startAt: LocalDate,
        endAt: LocalDate,
    ): Either<TaskUseCaseError, Task> {
        if (startAt.isAfter(endAt)) {
            return TaskUseCaseError.InvalidDateRange("startAt must be before endAt").left()
        }
        val task = Task.create(
            title = title,
            description = description,
            startAt = startAt,
            endAt = endAt
        )
        taskRepository.saveTask(task)
        return task.right()
    }
}

sealed class TaskUseCaseError {
    data class InvalidDateRange(val reason: String) : TaskUseCaseError()
}