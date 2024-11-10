package com.rkm.tasky.feature.agenda.model

sealed class AgendaItem {
    abstract val id: String
    abstract val title: String
    abstract val description: String
}

data class AgendaTaskItem(
    override val id: String,
    override val title: String,
    override val description: String,
    val time: Long,
    val isDone: Boolean
): AgendaItem()

data class AgendaEventItem(
    override val id: String,
    override val title: String,
    override val description: String,
    val to: Long,
    val from: Long
): AgendaItem()

data class AgendaReminderItem(
    override val id: String,
    override val title: String,
    override val description: String,
    val time: Long
): AgendaItem()