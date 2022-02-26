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

    @ExceptionHandler(value = [])
    fun handleConflict(exception: NotFoundException, request: WebRequest): ResponseEntity<Any> {
        val error = ErrorObject(
            status = HttpStatus.NOT_FOUND,
            type = "not-found",
            message = exception.message!!
        )

        return handleExceptionInternal(exception, exception.message, HttpHeaders(), error.status, request)
    }

    data class ErrorObject(val status: HttpStatus, val type: String, val message: String)
}
