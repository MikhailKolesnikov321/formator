package sfedu.net.formator.persistence.impl

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.Task
import sfedu.net.formator.domain.TaskId
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.generated.tables.Tasks.TASKS
import sfedu.net.formator.generated.tables.TaskUser.TASK_USER
import sfedu.net.formator.generated.tables.daos.TasksDao
import sfedu.net.formator.persistence.TaskRepository
import sfedu.net.formator.persistence.mappers.toDomain
import sfedu.net.formator.persistence.mappers.toEntity

@Component
class TaskRepositoryImpl(
    private val dslContext: DSLContext,
    private val taskDao: TasksDao
) : TaskRepository {

    override fun saveTask(task: Task): Task {
        val entity = task.toEntity()
        dslContext.transaction { ctx ->
            ctx.dsl()
                .insertInto(TASKS)
                .set(TASKS.ID, entity.id)
                .set(TASKS.TITLE, entity.title)
                .set(TASKS.DESCRIPTION, entity.description)
                .set(TASKS.START_AT, entity.startAt)
                .set(TASKS.END_AT, entity.endAt)
                .set(TASKS.CREATED_AT, entity.createdAt)
                .execute()
        }
        return task
    }

    override fun saveTaskAndUser(taskId: TaskId, userId: UserId) {
        dslContext.transaction { ctx ->
            ctx.dsl()
                .insertInto(TASK_USER)
                .set(TASK_USER.USER_ID, userId.uuidValue())
                .set(TASK_USER.TASK_ID, taskId.uuidValue())
                .onDuplicateKeyIgnore()
                .execute()
        }
    }

    override fun findById(id: TaskId): Task? {
        return taskDao.fetchOptionalById(id.value).map { it.toDomain() }.orElse(null)
    }

    override fun findAll(): List<Task> {
        return taskDao.findAll().map { it.toDomain() }
    }

    override fun deleteById(id: TaskId) {
        taskDao.deleteById(id.value)
    }

    override fun update(task: Task): Task {
        val entity = task.toEntity()
        dslContext.update(TASKS)
            .set(TASKS.TITLE, entity.title)
            .set(TASKS.DESCRIPTION, entity.description)
            .set(TASKS.START_AT, entity.startAt)
            .set(TASKS.END_AT, entity.endAt)
            .where(TASKS.ID.eq(entity.id))
            .execute()
        return task
    }
}