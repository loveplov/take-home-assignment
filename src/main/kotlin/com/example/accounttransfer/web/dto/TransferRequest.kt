package com.example.accounttransfer.web.dto

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Min

@Schema(description = "Model for a transfer request.")
data class TransferRequest (
	@field:Schema(
		description = "account from id",
		example = "6416b75e643a11ed81ce0242ac120002",
		type = "String",
		required = true
	)
	val accountFromId: String,

	@field:Schema(
		description = "account to id",
		example = "782c2e72643a11ed81ce0242ac120002",
		type = "String",
		required = true
	)
	val accountToId: String,

	@field:Parameter(name = "amount to transfer", example = "1000", required = true)
	@field:Schema(
		description = "amount to transfer",
		example = "782c2e72643a11ed81ce0242ac120002",
		type = "Int",
		minimum = "1",
		required = true,
	)
	@field:Min(1)
	val amount: Long)