package sfedu.net.formator.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import sfedu.net.formator.util.ValidationError
import java.time.Instant
import java.util.*

@JvmInline
value class ReportId(val value: UUID) {
    companion object {
        fun generate() = ReportId(UUID.randomUUID())
    }
}

data class ReportTitle private constructor(val value: String) {
    companion object {
        private const val MAX_LENGTH = 200

        fun from(input: String): Either<ReportError, ReportTitle> = when {
            input.isBlank() -> ReportError.EmptyTitle.left()
            input.length > MAX_LENGTH -> ReportError.TitleTooLong(MAX_LENGTH).left()
            else -> ReportTitle(input).right()
        }
    }
}

data class ReportPeriod(
    val start: Instant,
    val end: Instant
) {
    companion object {
        fun from(
            start: Instant,
            end: Instant
        ): Either<ReportError, ReportPeriod> = when {
            end.isBefore(start) -> ReportError.InvalidPeriod.left()
            else -> ReportPeriod(start, end).right()
        }
    }
}

enum class ReportStatus {
    DRAFT, SUBMITTED, APPROVED, REJECTED
}

sealed interface ReportError : ValidationError {
    data object EmptyTitle : ReportError {
        override val message = "Title cannot be empty"
    }
    data class TitleTooLong(val max: Int) : ReportError {
        override val message = "Title exceeds $max characters limit"
    }
    data object InvalidPeriod : ReportError {
        override val message = "End date must be after start date"
    }
}