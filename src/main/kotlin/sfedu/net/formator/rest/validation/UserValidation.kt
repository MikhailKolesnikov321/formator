package sfedu.net.formator.rest.validation

import arrow.core.EitherNel
import arrow.core.toEitherNel
import sfedu.net.formator.domain.Email
import sfedu.net.formator.domain.FullName
import sfedu.net.formator.domain.Username
import sfedu.net.formator.util.ValidationError

fun Username.Companion.validated(field: String, value: String): EitherNel<ValidationError, Username> {
    return from(value).mapLeft { it as ValidationError }.toEitherNel()
}

fun Email.Companion.validated(field: String, value: String): EitherNel<ValidationError, Email> {
    return from(value).mapLeft { it as ValidationError }.toEitherNel()
}

fun FullName.Companion.validated(field: String, value: String): EitherNel<ValidationError, FullName> {
    return aggregateName(value).mapLeft { it as ValidationError }.toEitherNel()
}