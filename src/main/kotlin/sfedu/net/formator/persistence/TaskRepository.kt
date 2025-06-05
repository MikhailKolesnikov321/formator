package sfedu.net.formator.persistence

import sfedu.net.formator.domain.Task
import sfedu.net.formator.domain.TaskId
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.persistence.impl.TaskView

interface TaskRepository {
    fun saveTask(task: Task): Task
    fun saveTaskAndUser(taskId: TaskId, userId: UserId, order: Int?, answer: String?)
    fun findById(id: TaskId): Task?
    fun findAll(): List<Task>
    fun deleteById(id: TaskId)
    fun update(task: Task): Task
    fun findAllAnswerForUser(userId: UserId): List<TaskView>
    // fun findTaskByUserId(userId: UserId): List<Task>
}
