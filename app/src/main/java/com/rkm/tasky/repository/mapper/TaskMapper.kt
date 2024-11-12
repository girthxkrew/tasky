package com.rkm.tasky.repository.mapper

import com.rkm.tasky.database.model.TaskEntity
import com.rkm.tasky.feature.task.model.Task
import com.rkm.tasky.network.model.request.TaskRequest
import com.rkm.tasky.network.model.response.TaskDTO
import com.rkm.tasky.util.date.toLocalDateTime
import com.rkm.tasky.util.date.toLong

fun TaskDTO.asTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt,
        isDone = this.isDone
    )
}

fun TaskDTO.asTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toLocalDateTime(),
        remindAt = this.remindAt.toLocalDateTime(),
        isDone = this.isDone
    )
}

fun TaskEntity.asTaskDTO(): TaskDTO {
    return TaskDTO(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt,
        isDone = this.isDone
    )
}

fun TaskEntity.asTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toLocalDateTime(),
        remindAt = this.remindAt.toLocalDateTime(),
        isDone = this.isDone
    )
}

fun Task.asTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toLong(),
        remindAt = this.remindAt.toLong(),
        isDone = this.isDone
    )
}

fun Task.asTaskyRequest(): TaskRequest {
    return TaskRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toLong(),
        remindAt = this.remindAt.toLong(),
        isDone = this.isDone
    )
}
