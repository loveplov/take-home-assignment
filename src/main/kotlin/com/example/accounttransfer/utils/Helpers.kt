package com.example.accounttransfer.utils

import java.util.*

object Helpers {

	fun newId(): String = UUID.randomUUID().toString().replace("-", "")
}