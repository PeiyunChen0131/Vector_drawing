package a2.views

import a2.constants.Constants.Companion.BUTTON_MAX_WIDTH
import a2.constants.Constants.Companion.BUTTON_MIN_WIDTH
import a2.constants.Constants.Companion.BUTTON_PREF_WIDTH
import javafx.scene.Node
import javafx.scene.control.ToggleButton

class StandardButton(caption: String?, n: Node?) : ToggleButton(caption, n) {
    init {
        isVisible = true
        minWidth = BUTTON_MIN_WIDTH
        prefWidth = BUTTON_PREF_WIDTH
        maxWidth = BUTTON_MAX_WIDTH
        minWidth = BUTTON_MIN_WIDTH
        prefWidth = BUTTON_PREF_WIDTH
        maxWidth = BUTTON_MAX_WIDTH
    }
}