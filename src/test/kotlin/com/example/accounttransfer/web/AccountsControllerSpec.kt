package com.example.accounttransfer.web

import com.example.accounttransfer.domain.Account
import com.example.accounttransfer.service.AccountsService
import com.example.accounttransfer.utils.DtoFactory.accountCreateRequest
import com.example.accounttransfer.utils.DtoFactory.transferRequest
import com.example.accounttransfer.utils.Helpers
import com.example.accounttransfer.utils.Helpers.newId
import com.example.accounttransfer.web.dto.AccountResponse
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import org.springframework.http.HttpStatus
import kotlin.random.Random

class AccountsControllerSpec : WordSpec() {

	private lateinit var subject: AccountsController
	private lateinit var accountsService: AccountsService

	override fun beforeEach(testCase: TestCase) {
		accountsService = mockk(relaxed = true)
		subject = AccountsController(accountsService)
	}

	init {
		"create" should {
			"map from request to service and to out dto" {

				val accountId = "newId"
				val balance = Random.nextLong()
				val account = Account(id = accountId, balance = balance)
				val request = accountCreateRequest(balance = balance)
				val expected = AccountResponse(account.id, account.balance)

				mockkObject(Helpers)
				every { newId() } returns accountId
				every { accountsService.save(any()) } returns account

				val result = subject.create(request)

				result.statusCode shouldBe HttpStatus.CREATED
				result.body?.shouldBeEqualToComparingFields(expected)

				val accountSlot = slot<Account>()

				verify(exactly = 1) { accountsService.save(capture(accountSlot)) }
				accountSlot.captured.balance shouldBe balance
				accountSlot.captured.id shouldBe accountId

			}
		}
		"findById" should {
			"map from request to service and to out dto" {

				val accountId = "newId"
				val balance = Random.nextLong()
				val account = Account(id = accountId, balance = balance)
				val expected = AccountResponse(account.id, account.balance)

				every { accountsService.findById(accountId) } returns account

				val result = subject.getById(accountId)

				result.statusCode shouldBe HttpStatus.OK
				result.body?.shouldBeEqualToComparingFields(expected)

			}
		}

		"transfer" should {
			"call service for transfer" {

				val accountFromId = "accountFromId"
				val accountToId = "accountToId"
				val balance = Random.nextLong()
				val request = transferRequest(accountFromId, accountToId, balance)
				every { accountsService.transfer(accountFromId, accountToId, balance) } returns Unit

				val result = subject.transfer(request)

				result.statusCode shouldBe HttpStatus.OK

			}
		}
	}
}
