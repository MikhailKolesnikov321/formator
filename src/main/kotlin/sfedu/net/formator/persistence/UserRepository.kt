package sfedu.net.formator.persistence

import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.User
import sfedu.net.formator.domain.UserId

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UserId): User?
    fun findAll(): List<User>
    fun deleteById(id: UserId)
    fun update(user: User): User
    fun userExists(email: Email): Boolean
}
