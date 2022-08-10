package a2.views

import a2.Main.Companion.bot
import a2.Main.Companion.dot1
import a2.Main.Companion.dot2
import a2.Main.Companion.saved
import a2.Main.Companion.shapeIdx
import a2.constants.Constants.Companion.BUTTON_PREF_WIDTH
import a2.constants.Constants.Companion.DELIMITER
import a2.constants.Constants.Companion.circleImage
import a2.constants.Constants.Companion.eraseImage
import a2.constants.Constants.Companion.fillImage
import a2.constants.Constants.Companion.lineImage
import a2.constants.Constants.Companion.rectImage
import a2.constants.Constants.Companion.selectImage
import a2.interfaces.Observer
import a2.models.SketchCircle
import a2.models.SketchLine
import a2.models.SketchRect
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.text.Text
import javafx.stage.Stage
import java.io.*
import kotlin.system.exitProcess


class SketchItToolBarView(shape: Group, indicator: Group): VBox() {
    var SIM = SketchItMenuView()

    var select: ToggleButton = StandardButton("Select", ImageView(selectImage))
    var erase: ToggleButton = StandardButton("Erase", ImageView(eraseImage))
    var drawLine: ToggleButton = StandardButton("Line", ImageView(lineImage))
    var drawCircle: ToggleButton = StandardButton("Circle", ImageView(circleImage))
    var drawRectangle: ToggleButton = StandardButton("Rectangle", ImageView(rectImage))
    var fill: ToggleButton = StandardButton("Fill", ImageView(fillImage))
    private var toolPicker = ToggleGroup()

    var cpLine = ColorPicker(Color.BLACK)
    var cpFill = ColorPicker(Color.WHITE)
    var lineLabel: Label = Label("Line Color", cpLine)
    var fillLabel: Label = Label("Fill Color", cpFill)

    var lineThickness: Line = Line()
    var strokeWidthLabel = Label("stroke width", lineThickness)
    var slider = Slider()

    var lineStyle = Label("Line Style")
    private var style0Line = Line()
    private var style1Line = Line()
    private var style2Line = Line()
    private var style3Line = Line()
    private var ToggleButton0 = StandardButton("style 0", style0Line)
    private var ToggleButton1 = StandardButton("style 1", style1Line)
    private var ToggleButton2 = StandardButton("style 2", style2Line)
    private var ToggleButton3 = StandardButton("style 3", style3Line)

    var styleList = ArrayList<ToggleButton>()
    var styleLineList = ArrayList<Line>()
    var curStyle = 0
    var linePicker = ToggleGroup()

    var startX = 0.0
    var startY = 0.0
    var endX = 0.0
    var endY = 0.0

//    var clickedX = 0.0
//    var clickedY = 0.0

    var list: ArrayList<Observer> = ArrayList()

    private val cut = Alert(
        Alert.AlertType.INFORMATION,
        "Nothing is selected", ButtonType.OK
    )

    private val b = Alert(
        Alert.AlertType.INFORMATION,
        "Program Name: Sketch It!\nAuthor: Peiyun Chen\nWatID: 20817850", ButtonType.CLOSE
    )

    private fun buildFromLog(s: String, _main: SketchItToolBarView): Observer? {
        val values: Array<String> = s.split(DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val shapeType = values[0].toInt()
        val disX = values[1].toDouble()
        val disY = values[2].toDouble()
        val lineStyle = values[3].toInt()
        val startX = values[4].toDouble()
        val startY = values[5].toDouble()
        val endX = values[6].toDouble()
        val endY = values[7].toDouble()
        val lineRed = values[8].toDouble()
        val lineGreen = values[9].toDouble()
        val lineBlue = values[10].toDouble()
        val fillRed = values[11].toDouble()
        val fillGreen = values[12].toDouble()
        val fillBlue = values[13].toDouble()
        val linWidth = values[14].toDouble()
        when (shapeType) {
            0 -> {
                val l = SketchLine(startX, startY, endX, endY, _main)
                l.lineStyle = lineStyle
                l.displacementX = disX
                l.displacementY = disY
                l.stroke = Color.color(lineRed, lineGreen, lineBlue)
                l.strokeWidth = linWidth
                l.strokeDashArray.setAll(_main.styleLineList[lineStyle].strokeDashArray)
                return l
            }
            1 -> {
                val c = SketchCircle(startX, startY, 5, Color.color(fillRed, fillGreen, fillBlue), _main)
                c.lineStyle = lineStyle
                c.radius = endX
                c.stroke = Color.color(lineRed, lineGreen, lineBlue)
                c.strokeWidth = linWidth
                c.fill = Color.color(fillRed, fillGreen, fillBlue)
                c.strokeDashArray.setAll(_main.styleLineList[lineStyle].strokeDashArray)
                return c
            }
            2 -> {
                val rect = SketchRect(startX, startY, 2, 3, _main)
                rect.width = endX
                rect.height = endY
                rect.lineStyle = lineStyle
                rect.stroke = Color.color(lineRed, lineGreen, lineBlue)
                rect.strokeWidth = linWidth
                rect.fill = Color.color(fillRed, fillGreen, fillBlue)
                rect.strokeDashArray.setAll(_main.styleLineList[lineStyle].strokeDashArray)
                return rect
            }
            else -> return null
        }
    }

    init {
        SIM.itemAbout.onAction = EventHandler {
            b.showAndWait()
        }

        select.toggleGroup = toolPicker
        erase.toggleGroup = toolPicker
        drawLine.toggleGroup = toolPicker
        drawCircle.toggleGroup = toolPicker
        drawRectangle.toggleGroup = toolPicker
        fill.toggleGroup = toolPicker
        select.isSelected = true

        val toolOnClicks = EventHandler<MouseEvent> {
            shapeIdx = -1
            notifyIndicator(indicator)
        }
        erase.onMouseClicked = toolOnClicks
        drawLine.onMouseClicked = toolOnClicks
        drawCircle.onMouseClicked = toolOnClicks
        drawRectangle.onMouseClicked = toolOnClicks
        fill.onMouseClicked = toolOnClicks
        setNewAction(shape, indicator)
        setQuitAction()
        setSaveAction()
        setLoadAction(shape, indicator)
        setCopyAction()
        setPasteAction(shape)
        setCutAction(shape, indicator)

        val colorAction = EventHandler<ActionEvent> {
            if (shapeIdx >= 0) {
                list[shapeIdx].notified(this)
            }
        }

        cpLine.onAction = colorAction
        cpFill.onAction = colorAction

        lineThickness.strokeWidth = 5.0
        lineThickness.startX = 0.0
        lineThickness.endX = BUTTON_PREF_WIDTH

        slider.min = 2.0
        slider.max = 20.0
        slider.value = 5.0
        slider.valueProperty().addListener { _, _, _ ->
            lineThickness.strokeWidth = slider.value
            if (shapeIdx >= 0) {
                list[shapeIdx].notified(this)
            }
        }

        styleLineList.add(style0Line)
        styleLineList.add(style1Line)
        styleLineList.add(style2Line)
        styleLineList.add(style3Line)
        for (i in 0..3) {
            styleLineList[i].strokeWidth = 3.0
            styleLineList[i].startX = 0.0
            styleLineList[i].endX = BUTTON_PREF_WIDTH
        }

        style1Line.strokeDashArray.addAll(50.0, 40.0, 10.0, 40.0)
        style2Line.strokeDashArray.addAll(60.0, 25.0, 60.0, 25.0)
        style3Line.strokeDashArray.addAll(20.0, 25.0, 20.0, 25.0)

        ToggleButton0.toggleGroup = linePicker
        ToggleButton1.toggleGroup = linePicker
        ToggleButton2.toggleGroup = linePicker
        ToggleButton3.toggleGroup = linePicker
        ToggleButton0.isSelected = true

        curStyle = 0
        styleList.add(ToggleButton0)
        styleList.add(ToggleButton1)
        styleList.add(ToggleButton2)
        styleList.add(ToggleButton3)

        for (i in 0..3) {
            styleList[i].onMouseReleased = EventHandler {
                for (j in 0..3) {
                    if (styleList[j].isSelected) {
                        curStyle = j
                        break
                    }
                }
                if (shapeIdx >= 0) {
                    list[shapeIdx].notified(this)
                }
            }
        }

        children.addAll(select, erase, drawLine, drawCircle,
            drawRectangle, fill, Separator(Orientation.HORIZONTAL),
            cpLine, lineLabel, Separator(Orientation.HORIZONTAL),
            cpFill, fillLabel, Separator(Orientation.HORIZONTAL),
            strokeWidthLabel, slider, Separator(Orientation.HORIZONTAL),
            lineStyle, ToggleButton0, ToggleButton1, ToggleButton2, ToggleButton3)
    }

    private fun setNewAction(shapes: Group, indicator: Group) {
        SIM.itemNew.onAction = EventHandler {
            if (!saved) {
                val stage = Stage()
                val text = Text("This page is not saved!\n" +
                        "Cancel it and save it\n" +
                        "Or Click Ok and you will lost all progress.")
                text.relocate(10.0, 30.0)
                val ok = Button("Ok")
                ok.prefWidth = 75.0
                ok.relocate(130.0, 105.0)
                val cancel = Button("Cancel")
                cancel.prefWidth = 75.0
                cancel.relocate(215.0, 105.0)
                val scene = Scene(
                    Pane(
                        text, ok, cancel
                    ), 300.0, 150.0
                )
                stage.scene = scene
                stage.title = "Dialog Box"
                stage.isResizable = false
                stage.isAlwaysOnTop = true
                stage.show()
                cancel.setOnAction { stage.close() }
                ok.setOnAction {
                    shapeIdx = -1
                    list.clear()
                    shapes.children.clear()
                    setSaveStatus(true)
                    notifyIndicator(indicator)
                    stage.close()
                }
            } else {
                shapeIdx = -1
                notifyIndicator(indicator)
                list.clear()
                shapes.children.clear()
            }
        }
    }

    private fun setQuitAction() {
        SIM.itemQuit.onAction = EventHandler {
            if (!saved) {
                val stage = Stage()
                val text = Text("This page is not saved!\n" +
                                        "Cancel it and save it\n" +
                                        "Or Click Ok and you will lost all progress.")
                text.relocate(10.0, 30.0)
                val ok = Button("Ok")
                val cancel = Button("Cancel")
                ok.prefWidth = 75.0
                ok.relocate(130.0, 105.0)
                cancel.prefWidth = 75.0
                cancel.relocate(215.0, 105.0)
                val scene = Scene(
                    Pane(
                        text, ok, cancel
                    ), 300.0, 150.0
                )
                stage.scene = scene
                stage.title = "Dialog Box"
                stage.isResizable = false
                stage.isAlwaysOnTop = true
                stage.show()
                cancel.setOnAction { stage.close() }
                ok.setOnAction {
                    exitProcess(0)
                }
            } else {
                exitProcess(0)
            }
        }
    }

    private fun setLoadAction(shapes: Group, indicators: Group) {
        SIM.itemLoad.onAction = EventHandler {
            val txt = if (saved) {
                "No unsaved changes. Load now?"
            } else {
                "This page is not saved!\n" +
                        "Cancel it and save it\n" +
                        "Or Click Ok and you will lost all progress."
            }
            val stage = Stage()
            val text = Text(txt)
            text.relocate(10.0, 30.0)
            val ok = Button("Ok")
            val cancel = Button("Cancel")
            ok.prefWidth = 75.0
            ok.relocate(130.0, 105.0)
            cancel.prefWidth = 75.0
            cancel.relocate(215.0, 105.0)
            val scene = Scene(
                Pane(
                    text, ok, cancel
                ), 300.0, 150.0
            )
            stage.scene = scene
            stage.title = "Dialog Box"
            stage.isResizable = false
            stage.isAlwaysOnTop = true
            stage.show()
            cancel.setOnAction { stage.close() }
            ok.setOnAction {
                shapeIdx = -1
                notifyIndicator(indicators)
                list.clear()
                shapes.children.clear()
                val file: FileReader?
                var reader: BufferedReader? = null
                try {
                    file = FileReader("data.txt")
                    reader = BufferedReader(file)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    var line: String?
                    if (reader != null) {
                        while (reader.readLine().also { line = it } != null) {
                            val l = buildFromLog(line!!, this)
                            list.add(l!!)
                            shapes.children.add(l as Node?)
                        }
                    }
                    setSaveStatus(true)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                stage.close()
            }
        }
    }

    private fun setSaveAction() {
        SIM.itemSave.onAction = EventHandler {
            var file: FileWriter? = null
            var writer: BufferedWriter? = null
            try {
                file = FileWriter("data.txt")
                writer = BufferedWriter(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                val size = list.size
                for (i in 0 until size) {
                    writer?.write(
                        list[i].toString()
                    )
                }
                writer?.close()
                file?.close()
                setSaveStatus(true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setCutAction(shapes: Group, indicators: Group) {
        SIM.itemCut.onAction = EventHandler {
            if (shapeIdx >= 0) {
                var file: FileWriter? = null
                var writer: BufferedWriter? = null
                try {
                    file = FileWriter("copy.txt")
                    writer = BufferedWriter(file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    writer!!.write(
                        list[shapeIdx].toString()
                    )
                    writer.close()
                    file!!.close()
                    list.removeAt(shapeIdx)
                    shapes.children.removeAt(shapeIdx)
                    shapeIdx = -1
                    notifyIndicator(indicators)
                    setSaveStatus(false)
                    cut.contentText = "Cut succeed!"
                    cut.showAndWait()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                cut.contentText = "Nothing is selected"
                cut.showAndWait()
            }
        }
    }

    private fun setCopyAction() {
        SIM.itemCopy.onAction = EventHandler {
            if (shapeIdx >= 0) {
                var file: FileWriter? = null
                var writer: BufferedWriter? = null
                try {
                    file = FileWriter("copy.txt")
                    writer = BufferedWriter(file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    writer!!.write(
                        list[shapeIdx].toString()
                    )
                    writer.close()
                    file!!.close()
                    cut.contentText = "Copy succeed!"
                } catch (e: IOException) {
                    e.printStackTrace()
                    cut.showAndWait()
                }
            } else {
                cut.contentText = "Nothing is selected"
                cut.showAndWait()
            }
        }
    }

    private fun setPasteAction(shapes: Group) {
        SIM.itemPaste.onAction = EventHandler {
            val file: FileReader?
            var reader: BufferedReader? = null
            try {
                file = FileReader("copy.txt")
                reader = BufferedReader(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                var line: String?
                while (reader!!.readLine().also { line = it } != null) {
                    val l = buildFromLog(line!!, this)
                    list.add(l!!)
                    shapes.children.add(l as Node?)
                }
                setSaveStatus(false)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            cut.contentText = "If you copied or cut something\n" +
                    "A copy was pasted to its original position.\n" +
                    "Otherwise, nothing was pasted."

            cut.showAndWait()
        }
    }

    private fun findShape(x: Double, y: Double, shapes: Group): Int {
        for (i in shapes.children.indices.reversed()) {
            if (shapes.children[i].contains(x, y)) return i
        }
        return -1
    }

    fun mousePressedHandler(event: MouseEvent, shapes: Group, indicators: Group) {
        startX = event.x
        startY = event.y
        shapeIdx = findShape(startX, startY, shapes)
        endX = event.x
        endY = event.y

        if (shapeIdx == -1) notifyIndicator(indicators)

        when (toolPicker.selectedToggle) {
            select -> {
                notifyIndicator(indicators)
                if (shapeIdx == -1) return
                val selectedItem = list[shapeIdx]
                cpLine.value = selectedItem.getLineColor()
                cpFill.value = selectedItem.getFillColor()
                slider.value = selectedItem.getLineWidth()
                setStyleButton(selectedItem.lineStyle)
            }
            erase -> {
                if (shapeIdx == -1) return
                setSaveStatus(false)
                list.removeAt(shapeIdx)
                shapes.children.removeAt(shapeIdx)
                shapeIdx = -1
                notifyIndicator(indicators)
            }
            drawLine -> {
                val newLine = SketchLine(startX, startY, endX, endY, this)
                newLine.notified(this)
                setSaveStatus(false)
                shapeIdx = list.size
                list.add(newLine)
                shapes.children.add(newLine)
                notifyIndicator(indicators)
            }
            drawCircle -> {
                val newCircle = SketchCircle(startX, startY, 5, cpFill.value, this)
                newCircle.notified(this)
                setSaveStatus(false)
                shapeIdx = list.size
                list.add(newCircle)
                shapes.children.add(newCircle)
                notifyIndicator(indicators)
            }
            drawRectangle -> {
                val newRect = SketchRect(startX, startY, 10, 5, this)
                newRect.notified(this)
                setSaveStatus(false)
                shapeIdx = list.size
                list.add(newRect)
                shapes.children.add(newRect)
                notifyIndicator(indicators)
            }
            fill -> {
                if (shapeIdx == -1) return
                setSaveStatus(false)
                list[shapeIdx].notified(this)
                shapeIdx = -1
                notifyIndicator(indicators)
            }
        }
    }

    fun mouseDraggedHandler(event: MouseEvent, indicators: Group) {
        if (shapeIdx >= 0) {
            if (select.isSelected) {
                list[shapeIdx].translate(event.x, event.y)
                setSaveStatus(false)
                notifyIndicator(indicators)
            } else if (drawLine.isSelected || drawCircle.isSelected || drawRectangle.isSelected) {
                list[shapeIdx].setSize(event.x, event.y)
                notifyIndicator(indicators)
                setSaveStatus(false)
            }
        }
    }

    fun mouseReleasedHandler(indicators: Group) {
        if (shapeIdx >= 0) {
            if (drawLine.isSelected || drawCircle.isSelected || drawRectangle.isSelected) {
                shapeIdx = -1
                setSaveStatus(false)
                notifyIndicator(indicators)
            }
        }
    }

    private fun notifyIndicator(indicators: Group) {
        indicators.children.clear()
        if (shapeIdx >= 0) {
            dot1.centerX = list[shapeIdx].getIndicatorX()
            dot1.centerY = list[shapeIdx].getIndicatorY()
            dot2.centerX = list[shapeIdx].getToolbarX()
            dot2.centerY = list[shapeIdx].getToolbarY()
            indicators.children.add(dot1)
            indicators.children.add(dot2)
        }
    }

    private fun setStyleButton(buttonNumber: Int) {
        styleList[buttonNumber].isSelected = true
        curStyle = buttonNumber
    }

    private fun setSaveStatus(stat: Boolean) {
        saved = stat
        if (stat) {
            bot.text = "No unsaved changes."
        } else {
            bot.text = "You have changes unsaved!"
        }
    }
}