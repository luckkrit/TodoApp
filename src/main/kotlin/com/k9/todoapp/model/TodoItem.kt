package com.k9.todoapp.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "todo_item")
open class TodoItem(
    @SequenceGenerator(name = "todoItemSeqGen", sequenceName = "todo_seq", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todoItemSeqGen")
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @Column(name = "task_name")
    open var taskName: String? = null,

    @Column(name = "is_finished")
    open var isFinished: Boolean? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    open var createdDate: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    open var updatedDate: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    open var deletedDate: Date? = null
) {
}