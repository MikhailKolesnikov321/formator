package sfedu.net.formator.domain

import java.time.LocalDate
import java.time.OffsetDateTime

data class Task(
    val id: TaskId,
    val title: Title,
    val description: Description,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val createdAt: OffsetDateTime = OffsetDateTime.now()
) {
    companion object {
        fun create(
            title: Title,
            description: Description,
            startAt: LocalDate,
            endAt: LocalDate
        ): Task {
            return Task(
                id = TaskId.generate(),
                title = title,
                description = description,
                startAt = startAt,
                endAt = endAt
            )
        }
    }
}
