package io.sandfish.parcels.controllers.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler
    fun handleConflict(exception: EntityNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val error = ErrorObject(
            status = HttpStatus.NOT_FOUND,
            type = "not-found",
            message = exception.message!!
        )

        return handleExceptionInternal(exception, exception.message, HttpHeaders(), error.status, request)
    }

    @ExceptionHandler
    fun handleUnauthorizedUserForDepartment(exception: UnauthorizedUserForDepartmentException, request: WebRequest): ResponseEntity<Any> {
        val error = ErrorObject(
            status = HttpStatus.UNAUTHORIZED,
            type = "unauthorized-user-for-department",
            message = exception.message!!
        )

        return handleExceptionInternal(exception, exception.message, HttpHeaders(), error.status, request)
    }

    @ExceptionHandler
    fun handleNoMatchingDepartmentFound(exception: NoMatchingDepartmentFoundException, request: WebRequest): ResponseEntity<Any> {
        val error = ErrorObject(
            status = HttpStatus.UNPROCESSABLE_ENTITY, //A bit out there but kinda matches
            type = "no-matching-department-found",
            message = exception.message!!
        )

        return handleExceptionInternal(exception, exception.message, HttpHeaders(), error.status, request)
    }

    @ExceptionHandler
    fun handleNoSignMatch(exception: NoSignMatchException, request: WebRequest): ResponseEntity<Any> {
        val error = ErrorObject(
            status = HttpStatus.UNPROCESSABLE_ENTITY, //A bit out there but kinda matches
            type = "no-sign-match",
            message = exception.message!!
        )

        return handleExceptionInternal(exception, exception.message, HttpHeaders(), error.status, request)
    }

    private data class ErrorObject(val status: HttpStatus, val type: String, val message: String)
}
