package a2.constants

import javafx.scene.image.Image

final class Constants {
    companion object {
        const val DELIMITER = ","
        const val EDLINE = "\n"
        const val tbx = 30.0
        const val tby = 105.0

        const val STAGE_WIDTH = 1280.0
        const val STAGE_HEIGHT = 760.0
        const val BUTTON_MIN_WIDTH = 50.0
        const val BUTTON_PREF_WIDTH = 100.0
        const val BUTTON_MAX_WIDTH = 200.0

        private const val ICON_SIZE = 20.0
        var selectImage: Image = Image("select.png", ICON_SIZE, ICON_SIZE, true, true)
        var eraseImage: Image = Image("eraser.png", ICON_SIZE, ICON_SIZE, true, true)
        var fillImage: Image = Image("fill.png", ICON_SIZE, ICON_SIZE, true, true)
        var lineImage: Image = Image("line.png", ICON_SIZE, ICON_SIZE, true, true)
        var circleImage: Image = Image("circle.png", ICON_SIZE, ICON_SIZE, true, true)
        var rectImage: Image = Image("rect.png", ICON_SIZE, ICON_SIZE, true, true)
    }
}