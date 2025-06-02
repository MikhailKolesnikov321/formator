package sfedu.net.formator.rest.validation

import arrow.core.EitherNel
import arrow.core.leftNel
import arrow.core.right
import arrow.core.toEitherNel
import sfedu.net.formator.domain.Description
import sfedu.net.formator.domain.Title
import sfedu.net.formator.util.SimpleValidationError
import sfedu.net.formator.util.ValidationError
import java.time.LocalDate

fun validateDateInterval(
    startAt: LocalDate,
    endAt: LocalDate,
    now: LocalDate = LocalDate.now()
): EitherNel<ValidationError, Pair<LocalDate, LocalDate>> = when {
    startAt.isBefore(now) ->
        SimpleValidationError("Start date cannot be in the past").leftNel()

    endAt.isBefore(startAt) || endAt.isEqual(startAt) ->
        SimpleValidationError("End date must be after start date").leftNel()

    else ->
        Pair(startAt, endAt).right()
}

fun Title.Companion.validated(value: String): EitherNel<ValidationError, Title> = from(value).mapLeft { it }.toEitherNel()

fun Description.Companion.validated(value: String): EitherNel<ValidationError, Description> = from(value).mapLeft { it }.toEitherNel()