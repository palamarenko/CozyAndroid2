package ua.palamarenko.cozyandroid2.base_fragment.navigation

import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.Popup

class CozyException(val popup: Popup? = null, val status : Int? = null ) : Exception()