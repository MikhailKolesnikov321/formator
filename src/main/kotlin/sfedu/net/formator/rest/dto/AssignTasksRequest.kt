package sfedu.net.formator.rest.dto

import java.util.*

data class AssignTasksRequest(
    val studentId: UUID,
    val taskIds: List<UUID>
)