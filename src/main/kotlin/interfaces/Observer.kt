package a2.interfaces

import a2.views.SketchItToolBarView
import javafx.scene.paint.Color

interface Observer {
    var lineStyle: Int
    fun notified(main: SketchItToolBarView)
    fun translate(displacementX: Double, displacementY: Double)
    fun setSize(endX: Double, endY: Double)

    fun getIndicatorX(): Double
    fun getIndicatorY(): Double
    fun getToolbarX(): Double
    fun getToolbarY(): Double

    fun getLineColor(): Color
    fun getFillColor(): Color
    fun getLineWidth(): Double
    override fun toString(): String
}