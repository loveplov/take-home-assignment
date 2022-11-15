package com.example.accounttransfer.service

import com.example.accounttransfer.domain.Account
import com.example.accounttransfer.repostitory.AccountsRepository
import com.example.accounttransfer.utils.DomainFactory.createAccount
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.random.Random

class AccountsServiceSpec : WordSpec() {

	private lateinit var subject: AccountsServiceImpl
	private lateinit var accountsRepository: AccountsRepository

	override suspend fun beforeEach(testCase: TestCase) {
		accountsRepository = mockk(relaxed = true)
		subject = AccountsServiceImpl(accountsRepository)
	}

	init {

		"transfer endpoint validation should throw illegal argument exception" should {
			"when to and from accounts are the same" {
				val fromAccountId = "1"
				val toAccountId = "1"
				val amount = 100L
				val exception = shouldThrow<IllegalArgumentException> {
					subject.transfer(fromAccountId, toAccountId, amount)
				}
				exception.message shouldBe "fromAccountId and toAccountId cannot be the same"
				verify(exactly = 0) { accountsRepository.findOneLocked(any()) }
				verify(exactly = 0) { accountsRepository.saveAndFlush(any()) }
			}

			"when from account has insufficient funds" {
				val fromAccountId = "1"
				val toAccountId = "2"
				val amount = 100L

				every { accountsRepository.findOneLocked(fromAccountId) } returns Optional.of(
					Account(fromAccountId, 50L)
				)
				every { accountsRepository.findOneLocked(toAccountId) } returns Optional.of(
					Account(
						toAccountId,
						100L
					)
				)


				val exception = shouldThrow<IllegalArgumentException> {
					subject.transfer(fromAccountId, toAccountId, amount)
				}
				exception.message shouldBe "Not enough money on account $fromAccountId"

				verify(exactly = 0) { accountsRepository.saveAndFlush(any()) }
			}
		}

		"save" should {
			"call repository" {
				val account = createAccount(balance = 50L)
				every { accountsRepository.saveAndFlush(any()) } returns account
				val result = subject.save(account)
				result shouldBe account
			}
		}
		"findById" should {
			val accountId = "1"
			val accountBalance = Random.nextLong()
			val account = createAccount(id = accountId, balance = accountBalance)
			"call repository to get one and return it" {
				every { accountsRepository.findById(any()) } returns Optional.of(account)
				val result = subject.findById(accountId)
				result shouldBe account
				verify(exactly = 1) { accountsRepository.findById(accountId) }
			}

			"call repository to get one and return null" {
				every { accountsRepository.findById(any()) } returns Optional.empty()
				val result = subject.findById(accountId)
				result shouldBe null
				verify(exactly = 1) { accountsRepository.findById(accountId) }
			}
		}
		"findByIdLocked" should {
			val accountId = "1"
			val accountBalance = Random.nextLong()
			val account = createAccount(id = accountId, balance = accountBalance)
			"call repository to get one locked and return it" {
				every { accountsRepository.findOneLocked(any()) } returns Optional.of(account)
				val result = subject.findByIdLocked(accountId)
				result shouldBe account
				verify(exactly = 1) { accountsRepository.findOneLocked(accountId) }
			}

			"call repository to get one locked and return null" {
				every { accountsRepository.findOneLocked(any()) } returns Optional.empty()
				val result = subject.findByIdLocked(accountId)
				result shouldBe null
				verify(exactly = 1) { accountsRepository.findOneLocked(accountId) }
			}
		}
	}
}
