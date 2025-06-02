package sfedu.net.formator.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import sfedu.net.formator.util.ValidationError
import java.time.LocalDate
import java.util.*

@JvmInline
value class TaskId(val value: UUID) {
    fun uuidValue() = value
    companion object {
        fun generate() = TaskId(UUID.randomUUID())
        fun from(id: UUID) = TaskId(id)
    }

    override fun toString(): String = "${this::class.simpleName}(id=$value)"
}

data class Title private constructor(val value: String) {
    companion object {
        fun from(input: String): Either<TaskError, Title> =
            when {
                input.isBlank() -> TaskError.TitleEmpty.left()
                input.length > 100 -> TaskError.TitleTooLong.left()
                else -> Title(input).right()
            }
    }
}

data class Description private constructor(val value: String) {
    companion object {
        fun from(input: String): Either<TaskError, Description> =
            when {
                input.isBlank() -> TaskError.DescriptionEmpty.left()
                else -> Description(input).right()
            }
    }
}

data class Answer private constructor(val value: String) {
    companion object {
        fun from(input: String): Either<TaskError, Answer> =
            when {
                input.isBlank() -> TaskError.AnswerEmpty.left()
                else -> Answer(input).right()
            }
    }
}

enum class TaskStatus {
    DRAFT,
    COMPLETED,
    NOT_STARTED;

    companion object {
        fun from(value: String): Either<TaskError, TaskStatus> =
            try {
                valueOf(value.uppercase()).right()
            } catch (e: IllegalArgumentException) {
                TaskError.InvalidStatus(value).left()
            }

        fun calculateStatus(
            task: Task,
            answer: String?,
            now: LocalDate = LocalDate.now()
        ): TaskStatus {
            return when {
                now.isBefore(task.startAt) -> NOT_STARTED
                now.isAfter(task.endAt) && answer.isNullOrBlank() -> NOT_STARTED
                now.isAfter(task.startAt) && now.isBefore(task.endAt) -> DRAFT
                !answer.isNullOrBlank() -> COMPLETED
                else -> NOT_STARTED
            }
        }
    }
}


sealed interface TaskError : ValidationError {
    data object TitleEmpty : TaskError {
        override val message = "Title cannot be empty"
    }

    data object TitleTooLong : TaskError {
        override val message = "Title cannot exceed 100 characters"
    }

    data object DescriptionEmpty : TaskError {
        override val message = "Description cannot be empty"
    }

    data object AnswerEmpty : TaskError {
        override val message = "Answer cannot be empty"
    }

    data class InvalidStatus(val value: String) : TaskError {
        override val message = "Invalid task status: $value"
    }
}

