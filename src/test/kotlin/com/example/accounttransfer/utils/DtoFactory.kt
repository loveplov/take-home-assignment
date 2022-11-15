package com.example.accounttransfer.utils

import com.example.accounttransfer.utils.Helpers.newId
import com.example.accounttransfer.web.dto.CreateAccountRequest
import com.example.accounttransfer.web.dto.TransferRequest

object DtoFactory {

	fun accountCreateRequest(balance: Long = 10000L) = CreateAccountRequest(balance)
	fun transferRequest(
		fromAccountId: String = newId(),
		toAccountId: String = newId(),
		amount: Long = 10000L
	) = TransferRequest(fromAccountId, toAccountId, amount)

}