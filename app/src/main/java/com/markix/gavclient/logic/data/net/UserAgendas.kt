package com.markix.gavclient.logic.data.net

import com.markix.gavclient.logic.data.IOCAgendaData

data class UserAgendasList (
    val agendas: Array<IOCAgendaData>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserAgendasList

        if (!agendas.contentEquals(other.agendas)) return false

        return true
    }

    override fun hashCode(): Int {
        return agendas?.contentHashCode() ?: 0
    }
}