package com.example.actividad2_ddam.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.actividad2_ddam.ui.screens.*

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val EVENT_LIST = "event_list"
    const val EVENT_FORM = "event_form"
    const val EVENT_EDIT = "event_edit/{eventId}"
    const val CALENDAR = "calendar"
    const val SETTINGS = "settings"

    fun eventEdit(eventId: Int) = "event_edit/$eventId"
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,

        // Transición general suave
        enterTransition = {
            fadeIn(
                animationSpec = tween(250)
            )
        },

        exitTransition = {
            fadeOut(
                animationSpec = tween(250)
            )
        },

        popEnterTransition = {
            fadeIn(
                animationSpec = tween(250)
            )
        },

        popExitTransition = {
            fadeOut(
                animationSpec = tween(250)
            )
        }
    ) {

        // -------------------------
        // SPLASH
        // -------------------------
        composable(
            route = Routes.SPLASH,

            exitTransition = {
                fadeOut(
                    animationSpec = tween(942)
                )
            }
        ) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // -------------------------
        // LOGIN
        // -------------------------
        composable(
            route = Routes.LOGIN,

            enterTransition = {
                fadeIn(
                    animationSpec = tween(942)
                )
            },

            exitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Up,

                    animationSpec = tween(400)
                ) + fadeOut(
                    animationSpec = tween(400)
                )
            }
        ) {
            LoginScreen(
                onIngresar = {
                    navController.navigate(Routes.EVENT_LIST) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // -------------------------
        // HOME / LISTA
        // -------------------------
        composable(
            route = Routes.EVENT_LIST,

            enterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Up,

                    animationSpec = tween(400)
                ) + fadeIn(
                    animationSpec = tween(400)
                )
            },

            exitTransition = {
                fadeOut(
                    animationSpec = tween(300)
                )
            },

            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(300)
                )
            },

            popExitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Down,

                    animationSpec = tween(400)
                ) + fadeOut(
                    animationSpec = tween(400)
                )
            }
        ) {
            EventListScreen(
                navController = navController,
                onAddEventClick = {
                    navController.navigate(Routes.EVENT_FORM)
                }
            )
        }


        // -------------------------
        // CREAR ACTIVIDAD
        // -------------------------
        composable(
            route = Routes.EVENT_FORM,

            enterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(300)
                )
            },

            exitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(300)
                )
            },

            popEnterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(300)
                )
            },

            popExitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(300)
                )
            }
        ) {
            EventFormScreen(
                navController = navController,
                onCerrar = {
                    navController.popBackStack()
                }
            )
        }


        // -------------------------
        // EDITAR ACTIVIDAD
        // -------------------------
        composable(
            route = Routes.EVENT_EDIT,

            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.IntType
                }
            ),

            enterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(300)
                )
            },

            exitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(300)
                )
            },

            popEnterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(300)
                )
            },

            popExitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->

            val eventId =
                backStackEntry
                    .arguments
                    ?.getInt("eventId")
                    ?: -1

            EventEditScreen(
                eventId = eventId,
                onCerrar = {
                    navController.popBackStack()
                }
            )
        }


        // -------------------------
        // CALENDARIO
        // -------------------------
        composable(
            route = Routes.CALENDAR,

            enterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(200)
                )
            },

            exitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(200)
                )
            },

            popEnterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(200)
                )
            },

            popExitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(200)
                )
            }
        ) {
            CalendarScreen(
                navController = navController
            )
        }


        // -------------------------
        // AJUSTES
        // -------------------------
        composable(
            route = Routes.SETTINGS,

            enterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(200)
                )
            },

            exitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(200)
                )
            },

            popEnterTransition = {
                slideIntoContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Right,

                    animationSpec = tween(200)
                )
            },

            popExitTransition = {
                slideOutOfContainer(
                    towards =
                        AnimatedContentTransitionScope
                            .SlideDirection.Left,

                    animationSpec = tween(200)
                )
            }
        ) {
            SettingsScreen(
                navController = navController,

                onCerrarSesion = {
                    navController.navigate(
                        Routes.LOGIN
                    ) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}