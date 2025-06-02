package sfedu.net.formator.persistence.mappers

import arrow.core.getOrElse
import sfedu.net.formator.domain.Description
import sfedu.net.formator.domain.Task
import sfedu.net.formator.domain.TaskId
import sfedu.net.formator.domain.Title
import sfedu.net.formator.generated.tables.pojos.Tasks

fun Tasks.toDomain(): Task {
    return Task(
        id = TaskId.from(id),
        title = Title.from(title).getOrElse { error("Invalid title: $title") },
        description = Description.from(description).getOrElse { error("Invalid description") },
        startAt = startAt,
        endAt = endAt,
        createdAt = createdAt
    )
}

fun Task.toEntity(): Tasks {
    return Tasks().apply {
        id = this@toEntity.id.value
        title = this@toEntity.title.value
        description = this@toEntity.description.value
        startAt = this@toEntity.startAt
        endAt = this@toEntity.endAt
        createdAt = this@toEntity.createdAt
    }
}