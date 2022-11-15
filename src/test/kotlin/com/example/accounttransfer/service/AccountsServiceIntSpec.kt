package com.example.accounttransfer.service

import com.example.accounttransfer.repostitory.AccountsRepository
import com.example.accounttransfer.utils.DomainFactory.createAccount
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@ActiveProfiles("test")
@DataJpaTest
@Import(
	AccountsServiceImpl::class
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountsServiceIntSpec(
	@Autowired val subject: AccountsServiceImpl,
	@Autowired val accountsRepository: AccountsRepository
) {


	@Test
	fun `save should sucessfully transfer money from one account to another`() {
		val accountBalance = Random.nextLong()
		val accountTo = createAccount(balance = accountBalance)
		accountsRepository.saveAndFlush(accountTo)
		val accountOnDb = accountsRepository.findById(accountTo.id).get()

		with(accountOnDb) {
			id shouldBe accountTo.id
			balance shouldBe (accountTo.balance)
		}
	}

	@Test
	fun `transfer should sucessfully transfer money from one account to another`() {

		val accountFromBalance = 1000L
		val accountToBalance = 500L
		val accountFrom = createAccount(balance = accountFromBalance)
		val accountTo = createAccount(balance = accountToBalance)
		val transferAmount = 100L

		accountsRepository.saveAndFlush(accountFrom)
		accountsRepository.saveAndFlush(accountTo)

		subject.transfer(accountFrom.id, accountTo.id, amount = transferAmount)

		val accountUpdatedTo = accountsRepository.findById(accountTo.id).get()

		with(accountUpdatedTo) {
			balance shouldBe (accountToBalance + transferAmount)
		}

		val accountUpdatedFrom = accountsRepository.findById(accountFrom.id).get()
		with(accountUpdatedFrom) {
			balance shouldBe (accountFromBalance - transferAmount)
		}
	}


	//I kinda failed to do a proper init test as spring jpa seem to mess with tx isolation level when many threads are spawned so i use the defaults created with initial migration.
	//I also tried to use jmeter and it works just fine.
	@Test
	fun `transfer should succesfully transfer money from one account to another with 10 threads`() {

		val accountFromId = "6416b75e643a11ed81ce0242ac120002"
		val accountFromBalance = 100000L
		val accountToId = "782c2e72643a11ed81ce0242ac120002"
		val transferAmount = 100L
		val thirdAccountToId = "239bdd26d8ed44e58dfe23fe34bb0a8c"

		val threads = 10

		val workerPool: ExecutorService = Executors.newFixedThreadPool(threads)
		for (i in 0..9) {
			if(i % 2 == 0) {
				workerPool.execute {
					subject.transfer(accountFromId, thirdAccountToId, amount = transferAmount)
				}
			} else {
				workerPool.execute {
					subject.transfer(accountFromId, accountToId, amount = transferAmount)
				}
			}
		}
		workerPool.awaitTermination(1000, TimeUnit.MILLISECONDS)

		val accountUpdatedTo = accountsRepository.findById(accountToId).get()
		with(accountUpdatedTo) {
			balance shouldBe (transferAmount * 5)
		}

		val accountUpdatedFrom = accountsRepository.findById(accountFromId).get()
		with(accountUpdatedFrom) {
			balance shouldBe (accountFromBalance - (transferAmount * threads))
		}

		val accountUpdatedThird = accountsRepository.findById(thirdAccountToId).get()
		with(accountUpdatedThird) {
			balance shouldBe (transferAmount * 5)
		}

	}


}