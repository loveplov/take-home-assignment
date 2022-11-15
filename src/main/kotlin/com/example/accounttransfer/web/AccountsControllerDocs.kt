package com.example.accounttransfer.web

import com.example.accounttransfer.web.dto.AccountResponse
import com.example.accounttransfer.web.dto.CreateAccountRequest
import com.example.accounttransfer.web.dto.TransferRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Accounts controller")
interface AccountsControllerDocs {

	@Operation(
		summary = "Creates a new account",
		responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "201 CREATED",
			description = "Account created"
		)]
	)
	fun create(request: CreateAccountRequest): ResponseEntity<AccountResponse>

	@Operation(
		summary = "Finds an account by id",
		responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Account found"
		),
			io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "404",
				description = "Account not found"
			)]
	)
	fun getById(id: String): ResponseEntity<AccountResponse>

	@Operation(
		summary = "Transfer from one account to another",
		responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Transfer successful"
		),
			io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "404",
				description = "Account not found"
			),
			io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description = "To and from accounts are the same"
			)]
	)

	fun transfer(request: TransferRequest): ResponseEntity<Void>


}