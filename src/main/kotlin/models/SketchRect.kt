package a2.models

import a2.Main.Companion.saved
import a2.constants.Constants.Companion.DELIMITER
import a2.constants.Constants.Companion.EDLINE
import a2.constants.Constants.Companion.tbx
import a2.constants.Constants.Companion.tby
import a2.interfaces.Observer
import a2.views.SketchItToolBarView
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class SketchRect(startX: Double, startY: Double, width: Int, height: Int, _main: SketchItToolBarView) :
    Rectangle(startX, startY, width.toDouble(), height.toDouble()), Observer {
    override var lineStyle = 0
    private var main = _main

    init {
        notified(_main)
    }

    override fun notified(main: SketchItToolBarView) {
        this.main = main
        lineStyle = this.main.curStyle
        this.main.styleList[lineStyle].isSelected = true
        stroke = this.main.cpLine.value
        fill = this.main.cpFill.value
        strokeWidth = this.main.slider.value
        strokeDashArray.setAll(this.main.styleLineList[this.main.curStyle].strokeDashArray)
        saved = false
    }

    override fun translate(displacementX: Double, displacementY: Double) {
        x = displacementX
        y = displacementY
    }

    override fun setSize(endX: Double, endY: Double) {
        width = endX - x
        height = endY - y
    }

    override fun toString(): String {
        val cpLineValue = stroke as Color
        val cpFillValue = fill as Color
        val sliderValue = strokeWidth
        val inform = listOf<Any>(
            2, 0, 0, lineStyle,
            x, y, width, height,
            cpLineValue.red, cpLineValue.green, cpLineValue.blue,
            cpFillValue.red, cpFillValue.green, cpFillValue.blue,
            sliderValue
        )
        return inform.joinToString(separator = DELIMITER, postfix = EDLINE)
    }

    override fun getIndicatorX(): Double {
        return x
    }

    override fun getIndicatorY(): Double {
        return y
    }

    override fun getToolbarX(): Double {
        return tbx
    }

    override fun getToolbarY(): Double {
        return tby + 60
    }

    override fun getLineColor(): Color {
        return stroke as Color
    }

    override fun getFillColor(): Color {
        return fill as Color
    }

    override fun getLineWidth(): Double {
        return strokeWidth
    }
}