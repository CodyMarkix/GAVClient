package com.markix.gavclient.logic.data

data class IOCAgendaData(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val status: Int? = null,
    val archived: Int? = null,
    val roles: IntArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IOCAgendaData

        if (id != other.id) return false
        if (status != other.status) return false
        if (archived != other.archived) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (!roles.contentEquals(other.roles)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (status ?: 0)
        result = 31 * result + (archived ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (roles?.contentHashCode() ?: 0)
        return result
    }
}

