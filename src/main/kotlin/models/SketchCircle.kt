package a2.models

import a2.Main.Companion.saved
import a2.constants.Constants.Companion.DELIMITER
import a2.constants.Constants.Companion.EDLINE
import a2.constants.Constants.Companion.tbx
import a2.constants.Constants.Companion.tby
import a2.interfaces.Observer
import a2.views.SketchItToolBarView
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import kotlin.math.sqrt

class SketchCircle(startX: Double, startY: Double, i: Int, value: Color?, _main: SketchItToolBarView) :
    Circle(startX, startY, i.toDouble(), value), Observer {
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

    // final position of the center
    override fun translate(displacementX: Double, displacementY: Double) {
        centerX = displacementX
        centerY = displacementY
    }

    override fun setSize(endX: Double, endY: Double) {
        radius = sqrt(
            (endX - centerX) * (endX - centerX) +
                    (endY - centerY) * (endY - centerY)
        )
    }

    override fun toString(): String {
        val cpLineValue = stroke as Color
        val cpFillValue = fill as Color
        val sliderValue = strokeWidth
        val inform = listOf<Any>(
            1, 0, 0, lineStyle,
            centerX, centerY, radius, 0,
            cpLineValue.red, cpLineValue.green, cpLineValue.blue,
            cpFillValue.red, cpFillValue.green, cpFillValue.blue,
            sliderValue
        )
        return inform.joinToString(separator = DELIMITER, postfix = EDLINE)
    }

    override fun getIndicatorX(): Double {
        return centerX
    }

    override fun getIndicatorY(): Double {
        return centerY
    }

    override fun getToolbarX(): Double {
        return tbx
    }

    override fun getToolbarY(): Double {
        return tby + 30
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