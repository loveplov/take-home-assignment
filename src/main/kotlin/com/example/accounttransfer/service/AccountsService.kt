package com.example.accounttransfer.service

import com.example.accounttransfer.domain.Account

interface AccountsService {
	fun save(account: Account): Account
	fun findById(id: String): Account?
	fun findByIdLocked(id: String): Account?
	fun transfer(from: String, to: String, amount: Long)
}