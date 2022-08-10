package a2.models

import a2.Main.Companion.saved
import a2.constants.Constants.Companion.DELIMITER
import a2.constants.Constants.Companion.EDLINE
import a2.constants.Constants.Companion.tbx
import a2.constants.Constants.Companion.tby
import a2.interfaces.Observer
import a2.views.SketchItToolBarView
import javafx.scene.paint.Color
import javafx.scene.shape.Line

class SketchLine internal constructor(startX: Double, startY: Double, endX: Double, endY: Double, _main: SketchItToolBarView) :
    Line(startX, startY, endX, endY), Observer {

    var main = _main
    var displacementX = 0.0
    var displacementY = 0.0
    override var lineStyle = 0

    init {
        notified(_main)
    }

    override fun notified(main: SketchItToolBarView) {
        this.main = main
        lineStyle = this.main.curStyle
        this.main.styleList[lineStyle].isSelected = true
        stroke = this.main.cpLine.value
        strokeWidth = this.main.slider.value
        strokeDashArray.setAll(this.main.styleLineList[this.main.curStyle].strokeDashArray)
        saved = false
    }

    override fun translate(displacementX: Double, displacementY: Double) {
        startX = displacementX
        startY = displacementY
        endX = displacementX + this.displacementX
        endY = displacementY + this.displacementY
    }

    override fun setSize(endX: Double, endY: Double) {
        setEndX(endX)
        setEndY(endY)
        displacementX = endX - startX
        displacementY = endY - startY
    }

    override fun toString(): String {
        val cpLineValue = stroke as Color
        val cpFillValue = stroke as Color
        val sliderValue = strokeWidth
        val inform = listOf<Any>(
            0, displacementX, displacementY, lineStyle,
            startX, startY, endX, endY,
            cpLineValue.red, cpLineValue.green, cpLineValue.blue,
            cpFillValue.red, cpFillValue.green, cpFillValue.blue,
            sliderValue
        )
        return inform.joinToString(separator = DELIMITER, postfix = EDLINE)
    }

    override fun getIndicatorX(): Double {
        return startX
    }

    override fun getIndicatorY(): Double {
        return startY
    }

    override fun getToolbarX(): Double {
        return tbx
    }

    override fun getToolbarY(): Double {
        return tby
    }

    override fun getLineColor(): Color {
        return stroke as Color
    }

    override fun getFillColor(): Color {
        return stroke as Color
    }

    override fun getLineWidth(): Double {
        return strokeWidth
    }
}