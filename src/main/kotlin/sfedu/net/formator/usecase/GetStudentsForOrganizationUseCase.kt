package sfedu.net.formator.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.UserId
import sfedu.net.formator.persistence.UserRepository
import java.util.*

@Component
class GetStudentsForOrganizationUseCase(
    private val userRepository: UserRepository
) {

    operator fun invoke(supervisorId: UserId): Either<GetStudentsForOrganizationUseCaseError, StudentsView> {
        userRepository.findById(supervisorId)
            ?: return GetStudentsForOrganizationUseCaseError.SupervisorNotFound("User with id: ${supervisorId.value} not found")
                .left()
        val result = userRepository.findStudentsBySupervisor(supervisorId)
        val viewItem = result.map {
            StudentViewItem(
                it.id.uuidValue(),
                it.fullName.getFullName()
            )
        }
        return StudentsView(supervisorId.uuidValue(), viewItem).right()
    }
}

sealed class GetStudentsForOrganizationUseCaseError {
    data class SupervisorNotFound(val message: String) : GetStudentsForOrganizationUseCaseError()
}

data class StudentsView(
    val supervisorId: UUID,
    val students: List<StudentViewItem>
)

data class StudentViewItem(
    val studentId: UUID,
    val name: String
)