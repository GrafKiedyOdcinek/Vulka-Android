package io.github.vulka.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.drawablepainter.DrawablePainter
import dev.medzik.android.compose.theme.combineAlpha
import dev.medzik.android.compose.theme.warningContainer
import io.github.vulka.ui.R
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val context = LocalContext.current

    // get app icon
    val icon = context.packageManager.getApplicationIcon(context.packageName)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Vulka",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = DrawablePainter(icon),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )

            Text(
                text = stringResource(R.string.WelcomeScreen_Title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                text = stringResource(R.string.WelcomeScreen_Description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            OutlinedButton(
                onClick = { navController.navigate(ChoosePlatform) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 90.dp)
                    .padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.GetStarted))
            }
        }
    }
}