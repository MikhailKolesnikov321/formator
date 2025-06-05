package sfedu.net.formator.application.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.UserId
import java.util.*

@Component
class CurrentUserProvider {

    fun getCurrentUserId(): UserId {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw RuntimeException("User not authenticated")

        val id = authentication.principal as? String
            ?: throw RuntimeException("Invalid authentication principal")

        return UserId.from(UUID.fromString(id))
    }
}