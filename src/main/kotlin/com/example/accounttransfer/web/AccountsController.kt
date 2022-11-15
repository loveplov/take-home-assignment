package com.example.accounttransfer.web

import com.example.accounttransfer.service.AccountsService
import com.example.accounttransfer.web.dto.AccountResponse
import com.example.accounttransfer.web.dto.CreateAccountRequest
import com.example.accounttransfer.web.dto.TransferRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityNotFoundException
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/accounts")
class AccountsController(val accountsService: AccountsService) : AccountsControllerDocs {

	@PostMapping(
		consumes = [MediaType.APPLICATION_JSON_VALUE],
		produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	@ResponseStatus(HttpStatus.CREATED)
	override fun create(@RequestBody @Valid request: CreateAccountRequest): ResponseEntity<AccountResponse> {
		return accountsService
			.save(request.toDomain())
			.let { AccountResponse.fromDomain(it) }
			.let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
	}

	@GetMapping(
		"/{id}",
		produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	@ResponseStatus(HttpStatus.OK)
	override fun getById(@PathVariable id: String): ResponseEntity<AccountResponse> =
		accountsService
			.findById(id)
			?.let { AccountResponse.fromDomain(it) }
			?.let { ResponseEntity.status(HttpStatus.OK).body(it) }
			?: throw EntityNotFoundException("Account with id $id not found")

	@PostMapping(
		"/transfer",
		consumes = [MediaType.APPLICATION_JSON_VALUE],
		produces = [MediaType.APPLICATION_JSON_VALUE]
	)
	override fun transfer(@RequestBody @Valid request: TransferRequest): ResponseEntity<Void> {
		accountsService.transfer(request.accountFromId, request.accountToId, request.amount)
		return ResponseEntity.ok().build()
	}

}