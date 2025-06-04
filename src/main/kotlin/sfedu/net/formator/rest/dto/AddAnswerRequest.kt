package sfedu.net.formator.rest.dto

import java.util.*

data class AddAnswerRequest(
    val taskId: UUID,
    val studentId: UUID,
    val answer: String,
)