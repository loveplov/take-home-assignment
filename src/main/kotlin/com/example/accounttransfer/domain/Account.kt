package com.example.accounttransfer.domain

import com.example.accounttransfer.utils.Helpers.newId
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "accounts")
class Account(
	@Id
	var id: String = newId(),
	var balance: Long,//in cents
)
