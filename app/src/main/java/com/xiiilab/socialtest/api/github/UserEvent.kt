package com.xiiilab.socialtest.api.github

data class UserEvent constructor(val id: Long, val type: String) {

    override fun equals(other: Any?): Boolean {
        return other is UserEvent && other.id == id && other.type == type
    }

    override fun hashCode(): Int {
        return id.toInt()
    }
}
