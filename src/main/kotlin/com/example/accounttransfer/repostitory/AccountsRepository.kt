package com.example.accounttransfer.repostitory

import com.example.accounttransfer.domain.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.LockModeType
import javax.persistence.QueryHint

@Repository
interface AccountsRepository : JpaRepository<Account, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "300000"))
	@Query("select a from Account a where a.id = ?1")
	fun findOneLocked(id: String): Optional<Account>
}