package com.xlebnick.kitties.utils

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.xlebnick.kitties.di.ActivityScoped
import com.xlebnick.kitties.di.FragmentScoped
import javax.inject.Inject

/**
 *  A navigation controller injected into fragments.
 */
@FragmentScoped
class FragmentNavControllerHelper @Inject constructor() : NavControllerHelper()

/**
 *  A navigation controller injected into activities.
 */
@ActivityScoped
class ActivityNavControllerHelper @Inject constructor() : NavControllerHelper()

/**
 *  A wrapper for NavController logic.
 */
open class NavControllerHelper {

    private var navController: NavController? = null

    fun setNavController(navController: NavController?) {
        this.navController = navController
    }

    /**
     * Performs navigation to the specified NavDirections with the animations from bottom or from left.
     *
     * @param direction target navigation destination
     * @param popupToOptions Provide a pair of destination id and inclusive
     */
    fun navigateTo(
        direction: NavDirections,
        popupToOptions: Pair<Int, Boolean>? = null,
        singleTop: Boolean = false,
    ) {
        var navOptions = NavOptions.Builder()
            .setLaunchSingleTop(singleTop)


        if (popupToOptions != null) {
            val destinationId = popupToOptions.first
            val inclusive = popupToOptions.second
            navOptions = navOptions.setPopUpTo(destinationId, inclusive)
        }

        navigateTo(direction, navOptions.build())
    }

    fun navigateTo(direction: NavDirections, navOptions: NavOptions?) {
        // Note: try catch here is because of not crashing if user clicks quickly, this probably is
        // not the good solution and needs to be resolved by not allowing onClickListener to be called
        // twice
        try {
            navController?.navigate(direction, navOptions)
        } catch (e: Exception) {
            Log.e("***", "navigateTo", e)
        }
    }

    fun navigateTo(@IdRes id: Int, args: Bundle? = null, navOptions: NavOptions? = null) {
        try {
            navController?.navigate(id, args, navOptions)
        } catch (e: Exception) {
            Log.e("***", "navigateTo", e)
        }
    }

    fun popBackStack(@IdRes id: Int, inclusive: Boolean): Boolean {
        return try {
            navController?.popBackStack(id, inclusive) ?: false
        } catch (e: Exception) {
            Log.e("***", "popBackStack", e)
            false
        }
    }

    fun popBackStack(): Boolean {
        return try {
            navController?.popBackStack() ?: false
        } catch (e: Exception) {
            Log.e("***", "popBackStack", e)
            false
        }
    }

    fun navigateUp(): Boolean {
        return try {
            navController?.navigateUp() ?: false
        } catch (e: Exception) {
            Log.e("***", "navigateUp", e)
            false
        }
    }

    fun getCurrentNavigationId(): Int? {
        return navController?.currentDestination?.id
    }

}

