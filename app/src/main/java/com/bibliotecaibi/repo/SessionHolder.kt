package com.bibliotecaibi.repo

data class Session(
    val ownerId: Long,
    val userId: Long,
    val role: String,
    val branchId: Long? = null
)

object SessionHolder {
    @Volatile
    var session: Session? = null
}
