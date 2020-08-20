package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups.CozyBottomSheets
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups.CozyFullPopup


fun AppCompatActivity.simpleInit(id : Int){
    val frameLayout = FrameLayout(this)
    frameLayout.id = View.generateViewId()
    this.setContentView(frameLayout)
    val finalHost = NavHostFragment.create(id)
    supportFragmentManager.beginTransaction()
        .replace(frameLayout.id, finalHost)
        .setPrimaryNavigationFragment(finalHost)
        .commit()
}


fun CozyViewModel.taskNavigate(fragment: Fragment, bundle: Bundle) {
    task(NAVIGATE, fragment, bundle)
}

fun CozyViewModel.taskNavigate(fragment: NavigateNewActivity, bundle: Bundle) {
    task(NAVIGATE, fragment, bundle)
}

fun CozyViewModel.taskNavigate(fragment: Int, bundle: Bundle) {
    task(NAVIGATE, fragment, bundle)
}

fun CozyViewModel.taskShowPopup(popup: Popup) {
    task(SHOW_POPUP, popup)
}

fun CozyViewModel.taskShowPopup(popup: CozyFullPopup<*>) {
    task(SHOW_POPUP, popup)
}

fun CozyViewModel.taskShowPopup(popup: CozyBottomSheets<*>) {
    task(SHOW_POPUP, popup)
}

fun CozyViewModel.taskShowPopup(popup: ShowPopupWindow) {
    task(SHOW_POPUP, popup)
}

fun CozyViewModel.taskShowPopup(popup: ShowPopupWindowCustomCell) {
    task(SHOW_POPUP, popup)
}

fun CozyViewModel.taskStartActivityForResult(data: StartActivityCallback, intentBundle: Bundle) {
    task(START_ACTIVITY_FOR_RESAULT, data, intentBundle)
}

fun CozyViewModel.taskStartActivityForResult(
    data: StartActivityTypeCallback<*>,
    intentBundle: Bundle
) {
    task(START_ACTIVITY_FOR_RESAULT, data, intentBundle)
}


fun CozyViewModel.taskStartActivity(data: Class<*>, intentBundle: Bundle) {
    task(START_ACTIVITY, data, intentBundle)
}

fun CozyViewModel.taskStartActivity(data: Intent) {
    task(START_ACTIVITY, data)
}

fun CozyViewModel.taskBackPress(fragment: Class<*>? = null) {
    if (fragment == null) {
        task(BACK_PRESS)
    } else {
        task(BACK_PRESS, fragment)
    }
}


/**
 * CozyFragment Extensions
 */


fun CozyFragment<*>.taskNavigate(fragment: Int, bundle: Bundle) {
    task(NAVIGATE, fragment, bundle)
}

fun CozyFragment<*>.taskNavigate(fragment: Fragment, bundle: Bundle) {
    task(NAVIGATE, fragment, bundle)
}

fun CozyFragment<*>.taskNavigate(fragment: NavigateNewActivity, bundle: Bundle) {
    task(NAVIGATE, fragment, bundle)
}

fun CozyFragment<*>.taskShowPopup(popup: Popup) {
    task(SHOW_POPUP, popup)
}

fun CozyFragment<*>.taskShowPopup(popup: CozyFullPopup<*>) {
    task(SHOW_POPUP, popup)
}

fun CozyFragment<*>.taskShowPopup(popup: CozyBottomSheets<*>) {
    task(SHOW_POPUP, popup)
}

fun CozyFragment<*>.taskShowPopup(popup: ShowPopupWindow) {
    task(SHOW_POPUP, popup)
}

fun CozyFragment<*>.taskShowPopup(popup: ShowPopupWindowCustomCell) {
    task(SHOW_POPUP, popup)
}

fun CozyFragment<*>.taskStartActivityForResult(data: StartActivityCallback, intentBundle: Bundle) {
    task(START_ACTIVITY_FOR_RESAULT, data, intentBundle)
}

fun CozyFragment<*>.taskStartActivityForResult(
    data: StartActivityTypeCallback<*>,
    intentBundle: Bundle
) {
    task(START_ACTIVITY_FOR_RESAULT, data, intentBundle)
}


fun CozyFragment<*>.taskStartActivity(data: Class<*>, intentBundle: Bundle) {
    task(START_ACTIVITY, data, intentBundle)
}

fun CozyFragment<*>.taskStartActivity(data: Intent) {
    task(START_ACTIVITY, data)
}

fun CozyFragment<*>.taskBackPress(fragment: Class<*>? = null) {
    if (fragment == null) {
        task(BACK_PRESS)
    } else {
        task(BACK_PRESS, fragment)
    }
}
