package a2

import a2.constants.Constants.Companion.STAGE_HEIGHT
import a2.constants.Constants.Companion.STAGE_WIDTH
import a2.views.SketchItToolBarView
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.stage.Stage


class Main : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.maxWidth = 1600.0
        primaryStage.maxHeight = 1200.0
        primaryStage.minWidth = 720.0
        primaryStage.minHeight = 470.0

        primaryStage.x = 200.0
        primaryStage.y = 280.0
        primaryStage.width = STAGE_WIDTH
        primaryStage.height = STAGE_HEIGHT
        primaryStage.isResizable = true

        primaryStage.title = "Sketch It!"

        val root = BorderPane()

        val shapes = Group()
        val indicators = Group()
        root.children.add(shapes)

        val toolBar = SketchItToolBarView(shapes, indicators)
        root.left = toolBar
        root.top = toolBar.SIM

        root.bottom = bot
        root.children.add(indicators)

        val scene = Scene(root, 1280.0, 720.0)

        scene.setOnMousePressed { event -> toolBar.mousePressedHandler(event, shapes, indicators) }
        scene.setOnMouseDragged { event -> toolBar.mouseDraggedHandler(event, indicators) }
        scene.setOnMouseReleased { toolBar.mouseReleasedHandler(indicators) }

        scene.onKeyPressed = EventHandler { KeyEvent: KeyEvent ->
            if (toolBar.select.isSelected) {
                if (KeyEvent.code == KeyCode.ESCAPE) {
                    toolBar.select.isSelected = false
                    shapeIdx = -1
                    indicators.children.clear()
                } else if (KeyEvent.code == KeyCode.DELETE) {
                    toolBar.select.isSelected = false
                    toolBar.list.removeAt(shapeIdx)
                    shapes.children.removeAt(shapeIdx)
                    shapeIdx = -1
                    indicators.children.clear()
                }
            }
        }
        primaryStage.scene = scene
        primaryStage.show()
    }

    companion object {
        var saved: Boolean = false
        var shapeIdx = -1
        var dot1 = Circle(6.0, 6.0, 4.0, Color.BLUE)
        var dot2 = Circle(6.0, 6.0, 4.0, Color.BLUE)
        val bot = Text("No unsaved changes.")
    }



}