package com.xiiilab.socialtest.auth

/**
 * Created by XIII-th on 04.09.2018
 */
@Suppress("DataClassPrivateConstructor")
data class AuthResult private constructor(val state: AuthState, val error: String? = null) {

    companion object {
        val SUCCESS = AuthResult(AuthState.SUCCESS)
        fun error(message: String) = AuthResult(AuthState.FAILED, message)
        fun error(messageSupplier: () -> String) = error(messageSupplier.invoke())
    }
}