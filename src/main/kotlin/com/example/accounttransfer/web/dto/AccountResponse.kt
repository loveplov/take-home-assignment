package com.example.accounttransfer.web.dto

import com.example.accounttransfer.domain.Account
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Model for a account representation.")
data class AccountResponse(
	@field:Schema(
		description = "account id",
		example = "6416b75e643a11ed81ce0242ac120002",
		type = "String"
	)
	val id: String,

	@field:Schema(
		description = "account balance, in cents",
		example = "6416b75e643a11ed81ce0242ac120002",
		type = "Int"
	)
	val balance: Long){
	companion object {
		fun fromDomain(account: Account) = AccountResponse(account.id, account.balance)
	}
}