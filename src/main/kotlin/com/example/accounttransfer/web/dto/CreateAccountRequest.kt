package com.example.accounttransfer.web.dto

import com.example.accounttransfer.domain.Account
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Min

@Schema(description = "Model for account creation.")
data class CreateAccountRequest(
	@field:Schema(
		description = "account balance, in cents",
		example = "1000",
		type = "String"
	)
	@field:Min(0)
	@param:JsonProperty("balance")
	val balance: Long
) {
	fun toDomain(): Account {
		return Account(balance = balance)
	}
}