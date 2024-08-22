package io.github.vulka.impl.vulcan.hebe.types

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HebeAverageGrade(
    @SerialName("Id")
    val id: Int,
    @SerialName("PupilId")
    val pupilId: Int,
    @SerialName("PeriodId")
    val periodId: Int,
    @SerialName("Subject")
    val subject: HebeSubject,
    @SerialName("Average")
    val average: String?,
    @SerialName("Points")
    @Contextual
    val points: JsonElement? = null,
    @SerialName("Scope")
    val scope: String
)
