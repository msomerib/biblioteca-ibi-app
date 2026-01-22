package com.bibliotecaibi.util

import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom
import java.util.Base64

object Security {

    fun genSalt(size: Int = 16): String {
        val bytes = ByteArray(size)
        SecureRandom().nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun hashPassword(password: String, salt: String): String {
        return DigestUtils.sha256Hex("$salt:$password")
    }

    fun verify(password: String, salt: String, hash: String): Boolean {
        return hashPassword(password, salt) == hash
    }
}
