package ua.palamarenko.cozyandroid2.base_fragment.navigation

/**
 * If Fragment implement this interface, Back  event first of all call method onBackPress()
 * and if return is true default back event don't call
 */

interface BackPress {
    fun onBackPress() : Boolean{
        return false
    }
}
