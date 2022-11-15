package com.example.accounttransfer.service

import com.example.accounttransfer.domain.Account
import com.example.accounttransfer.repostitory.AccountsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class AccountsServiceImpl(val repository: AccountsRepository) : AccountsService {

	@Transactional
	override fun save(account: Account): Account {
		return repository.saveAndFlush(account)
	}

	override fun findById(id: String): Account? {
		return repository.findById(id).orElse(null)
	}

	override fun findByIdLocked(id: String): Account? {
		return repository.findOneLocked(id).orElse(null)
	}

	@Transactional
	override fun transfer(from: String, to: String, amount: Long) {
		if (from == to) {
			throw IllegalArgumentException("fromAccountId and toAccountId cannot be the same")
		}
		val fromAccount: Account
		val toAccount: Account
		if(from < to) {
			fromAccount =
				findByIdLocked(from) ?: throw EntityNotFoundException("Account $from not found")
			toAccount = findByIdLocked(to) ?: throw EntityNotFoundException("Account $to not found")
		} else {
			toAccount = findByIdLocked(to) ?: throw EntityNotFoundException("Account $to not found")
			fromAccount =
				findByIdLocked(from) ?: throw EntityNotFoundException("Account $from not found")
		}



		if (fromAccount.balance < amount) {
			throw IllegalArgumentException("Not enough money on account $from")
		}

		fromAccount.balance = fromAccount.balance - amount
		toAccount.balance = toAccount.balance + amount

		repository.saveAndFlush(fromAccount)
		repository.saveAndFlush(toAccount)
		return
	}

}