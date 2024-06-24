package io.github.vulka.core.api.types

import java.time.LocalDate

data class Grade(
    val value: String?,
    val weight: Float,
    val name: String,
    val date: LocalDate,
    val subject: String,
    val teacher: Teacher
)
