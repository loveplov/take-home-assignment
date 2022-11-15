package com.example.accounttransfer.utils

import com.example.accounttransfer.domain.Account
import com.example.accounttransfer.utils.Helpers.newId

object DomainFactory {

	fun createAccount(
		id: String = newId(),
		balance: Long = 10000L
	) = Account(id, balance)
}