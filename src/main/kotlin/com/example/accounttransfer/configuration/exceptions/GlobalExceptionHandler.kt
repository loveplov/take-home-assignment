package com.example.accounttransfer.configuration.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class GlobalExceptionHandler(private val mapper: ObjectMapper) {

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidationError(ex: MethodArgumentNotValidException): ResponseEntity<String> {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(mapper.writeValueAsString(ex.message))
	}

	@ResponseBody
	@ExceptionHandler(EntityNotFoundException::class)
	fun handleNotFound(
		ex: EntityNotFoundException,
		response: HttpServletResponse
	): ResponseEntity<String> {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(mapper.writeValueAsString(ex.message))
	}

	@ResponseBody
	@ExceptionHandler(IllegalArgumentException::class)
	fun handleIllegalArgument(
		ex: IllegalArgumentException,
		response: HttpServletResponse
	): ResponseEntity<String> {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(mapper.writeValueAsString(ex.message))
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception::class)
	fun handleException(ex: Exception): ResponseEntity<String> {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(mapper.writeValueAsString(ex.message))
	}


	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException::class)
	fun handleValidationError(ex: HttpRequestMethodNotSupportedException): String? {
		return ex.message
	}

}
