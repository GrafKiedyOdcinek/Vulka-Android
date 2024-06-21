package io.github.vulka.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.medzik.android.compose.icons.TopAppBarBackIcon
import dev.medzik.android.compose.navigation.NavigationAnimations
import io.github.vulka.core.api.Platform
import io.github.vulka.ui.common.DefaultScaffold
import io.github.vulka.ui.common.MediumTopAppBarWithBack
import io.github.vulka.ui.screens.ChoosePlatform
import io.github.vulka.ui.screens.Welcome
import io.github.vulka.ui.screens.WelcomeScreen
import io.github.vulka.ui.screens.auth.ChooseStudents
import io.github.vulka.ui.screens.auth.ChooseStudentsScreen
import io.github.vulka.ui.screens.auth.Login
import io.github.vulka.ui.screens.auth.LoginScreen
import io.github.vulka.ui.screens.dashboard.Home
import io.github.vulka.ui.screens.dashboard.HomeScreen
import io.github.vulka.ui.screens.dashboard.more.AccountManager
import io.github.vulka.ui.screens.dashboard.more.AccountManagerScreen
import io.github.vulka.ui.screens.dashboard.more.LuckyNumber
import io.github.vulka.ui.screens.dashboard.more.LuckyNumberScreen
import io.github.vulka.ui.utils.navtype.PlatformType
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VulkaNavigation(viewModel: VulkaViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    fun getStartDestination(): Any {
        val credentials = viewModel.credentialRepository.get()

        return if (credentials != null) {
            Home(
                platform = credentials.platform,
                userId = credentials.id.toString(),
                credentials = credentials.data,
                firstSync = false
            )
        } else {
            Welcome
        }
    }

    NavHost(
        navController,
        startDestination = getStartDestination(),
        modifier = Modifier.imePadding(),
        enterTransition = {
            NavigationAnimations.enterTransition()
        },
        exitTransition = {
            NavigationAnimations.exitTransition()
        },
        popEnterTransition = {
            NavigationAnimations.popEnterTransition()
        },
        popExitTransition = {
            NavigationAnimations.popExitTransition()
        }
    ) {
        composable<Welcome> {
            WelcomeScreen(navController)
        }

        composable<ChoosePlatform> {
            DefaultScaffold(
                topBar = {
                    TopBarWithBack(R.string.SelectJournal, navController)
                },
                horizontalPadding = false
            ) {
                ChoosePlatform(navController)
            }
        }

        composable<Login>(
            typeMap = mapOf(typeOf<Platform>() to PlatformType)
        ) {
            val args = it.toRoute<Login>()

            DefaultScaffold(
                topBar = {
                    MediumTopAppBarWithBack(
                        title = R.string.Login,
                        navController
                    )
                }
            ) {
                LoginScreen(args, navController)
            }
        }

        composable<ChooseStudents>(
            typeMap = mapOf(typeOf<Platform>() to PlatformType)
        ) {
            val args = it.toRoute<ChooseStudents>()

            DefaultScaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.ChooseStudents)
                            )
                        }
                    )
                }
            ) {
                ChooseStudentsScreen(args, navController)
            }
        }

        composable<Home>(
            typeMap = mapOf(typeOf<Platform>() to PlatformType)
        ) {
            val args = it.toRoute<Home>()

            HomeScreen(args, navController)
        }

        composable<AccountManager>(
            typeMap = mapOf(typeOf<Platform>() to PlatformType)
        ) {
            DefaultScaffold(
                topBar = {
                    TopBarWithBack(
                        title = R.string.AccountManager,
                        navController
                    )
                }
            ) {
                val args = it.toRoute<AccountManager>()

                AccountManagerScreen(args, navController)
            }
        }

        composable<LuckyNumber> {
            DefaultScaffold(
                topBar = {
                    TopBarWithBack(
                        title = R.string.LuckyNumber,
                        navController
                    )
                }
            ) {
                val args = it.toRoute<LuckyNumber>()

                LuckyNumberScreen(args)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBack(@StringRes title: Int, navController: NavController) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        navigationIcon = { TopAppBarBackIcon(navController) }
    )
}
