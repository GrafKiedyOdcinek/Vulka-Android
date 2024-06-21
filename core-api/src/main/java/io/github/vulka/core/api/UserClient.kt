package io.github.vulka.core.api

import io.github.vulka.core.api.types.Grade
import io.github.vulka.core.api.types.Lesson
import io.github.vulka.core.api.types.Student
import java.time.LocalDate
import java.util.Date

interface UserClient {
    suspend fun getStudents(): Array<Student>
    suspend fun getLuckyNumber(student: Student, date: Date): Int
    // TODO: add period selecting
    suspend fun getGrades(student: Student): Array<Grade>
    suspend fun getLessons(student: Student, dateFrom: LocalDate = LocalDate.now(), dateTo: LocalDate = dateFrom): Array<Lesson>
}
