package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.persistence.TaskRepository
import sfedu.net.formator.persistence.UserRepository
import java.io.ByteArrayOutputStream

@Component
class CreateReportUseCase(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(
        studentId: UserId,
        organisationId: UserId
    ): Either<CreateReportUseCaseError, Pair<String, ByteArray>> {
        val supervisor = userRepository.findById(organisationId)
            ?: return CreateReportUseCaseError.StudentNotFound("User with id: $organisationId not found")
                .left()
        val student = userRepository.findById(studentId)
            ?: return CreateReportUseCaseError.StudentNotFound("User with id: $studentId not found")
                .left()
        val tasks = taskRepository.findAllAnswerForUser(studentId)
        val template = javaClass.getResourceAsStream("/template.docx")
        val document = XWPFDocument(template)
        document.paragraphs.forEach { paragraph ->
            paragraph.runs.forEach { run ->
                val text = run.text()
                run.setText(text.replace("\${studentName}", student.fullName.getFullName()), 0)
            }
        }
        val outputStream = ByteArrayOutputStream()
        document.write(outputStream)
        outputStream.close()
        return ("Reportik.docx" to outputStream.toByteArray()).right()
    }
}

sealed class CreateReportUseCaseError {
    data class StudentNotFound(val message: String) : CreateReportUseCaseError()
    data class OrganizationSupervisorNotFound(val message: String) : CreateReportUseCaseError()
    data class TasksNotFound(val message: String) : CreateReportUseCaseError()
}