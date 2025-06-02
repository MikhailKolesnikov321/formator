package sfedu.net.formator.rest.dto

import java.time.LocalDate
import java.util.*

data class CreateTaskRequest(
    val title: String,
    val description: String,
    val startAt: LocalDate,
    val endAt: LocalDate,
)

data class CreateTaskResponse(
    val id: UUID,
)