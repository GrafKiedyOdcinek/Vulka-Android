package io.github.vulka.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.medzik.android.compose.rememberMutable
import dev.medzik.android.compose.ui.IconBox
import dev.medzik.android.compose.ui.bottomsheet.rememberBottomSheetState
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.types.Grade
import io.github.vulka.core.api.types.Student
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.common.Avatar
import io.github.vulka.ui.screens.dashboard.more.LuckyNumberBottomSheet
import io.github.vulka.ui.utils.getInitials
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
class Start(
    val platform: Platform,
    val userId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    args: Start,
    pullRefresh: @Composable BoxScope.() -> Unit = {},
    pullToRefreshState: PullToRefreshState,
    refreshed: Boolean,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    var luckyNumber by rememberMutable(0)

    val student by rememberMutable(viewModel.credentialRepository.getById(UUID.fromString(args.userId))!!.student)

    fun updateUI() {
        luckyNumber = viewModel.luckyNumberRepository.get(UUID.fromString(args.userId))?.number ?: 0
    }

    if (refreshed)
        updateUI()

    Box(
        modifier = Modifier
            .nestedScroll(connection = pullToRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            item {
                HeaderCard(student)
            }

            item {
                Row {
                    LuckyCard(luckyNumber)
                }
            }

            item {
                GradesCard(UUID.fromString(args.userId))
            }

            item {
                TimetableCard(UUID.fromString(args.userId))
            }
        }

        pullRefresh()
    }
}


@Composable
fun HeaderCard(student: Student) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 5.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(10.dp),
        ) {
            Avatar(text = student.getInitials())
            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    fontSize = 18.sp,
                    text = student.fullName
                )
                Text(student.classId.orEmpty())
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyCard(luckyNumber: Int) {
    val bottomSheetState = rememberBottomSheetState()

    Surface(
        modifier = Modifier.padding(horizontal = 3.dp, vertical = 5.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        onClick = { bottomSheetState.show() }
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 48.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBox(Icons.Default.Star)

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 18.sp,
                text = "${if (luckyNumber != 0) luckyNumber else stringResource(R.string.None)}"
            )
        }
    }

    LuckyNumberBottomSheet(
        bottomSheetState = bottomSheetState,
        luckyNumber = luckyNumber
    )
}

@Composable
fun GradesCard(
    userId: UUID,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    val gradesDb = viewModel.gradesRepository.getFromLastWeek(userId, LocalDate.now().minusWeeks(1))

    val gradeList: List<Grade> = gradesDb.map { it.grade }
    val uniqueSubjectNames: Set<String> = gradeList.map { it.subject }.sortedBy { it }.toSet()

    DashboardCard(
        icon = Icons.Default.Looks6,
        title = stringResource(R.string.LatestGrades)
    ) {
        if (gradeList.isNotEmpty()) {
            uniqueSubjectNames.forEach { subject ->
                Column {
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp).wrapContentSize()
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 5.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = if (subject.length > 25) subject.substring(
                                0,
                                25
                            ) + "..." else subject
                        )
                        val filterGrades = gradeList.filter { it.subject == subject }

                        for (grade in filterGrades) {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .size(25.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = grade.value.orEmpty(),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                modifier = Modifier.padding(vertical = 7.dp),
                text = stringResource(R.string.NoLatestGrades)
            )
        }
    }
}

@Composable
fun TimetableCard(
    userId: UUID,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    DashboardCard(
        icon = Icons.Default.Backpack,
        title = stringResource(R.string.Lessons)
    ) {
        val lessons = viewModel.timetableRepository.getByDateAndCredentialsId(userId, LocalDate.now())
            .map { it.lesson }.sortedBy { it.position }
        if (lessons.isNotEmpty()) {
            for (lesson in lessons) {
                Text(text = "${lesson.position}. ${lesson.subjectName}" + (if (lesson.classRoom != null) " (${lesson.classRoom})" else ""))
            }
        } else {
            Text(text = stringResource(R.string.NoLessons))
        }
    }
}

@Composable
fun DashboardCard(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 5.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row {
                IconBox(
                    modifier = Modifier.padding(end = 5.dp),
                    imageVector = icon
                )

                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    text = title
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}