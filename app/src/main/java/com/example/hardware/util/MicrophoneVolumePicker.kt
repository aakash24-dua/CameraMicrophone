
package com.example.hardware.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import kotlin.math.roundToInt

class MicrophoneVolumePicker(context: Context?) :
    View(context) {
    var paint = Paint()
    val GREEN = 8453888 // 80FF00
    val ORANGE = 16744448 // FF8000
    val RED = 14549506 // DE0202
    private val _isMono = false

    // margini dal bordo
    val PADDING_TOP = 30
    val PADDING_LEFT = 30
    val PADDING_RIGHT = 30
    val PADDING_BOTTOM = 30

    // DRAWING AREAS
    private var _canvasWidth // in android canvas.getWidth()
            = 0
    private var _canvasHeight // in android canvas.getHeight()
            = 0

    // area disponibile per il disegno
    private var _drawableAreaHeight = 0
    private var _drawableAreaWidth = 0

    // noise & cliping value
    val FULL_SCALE = 120
    var NOISE_THRESHOLD = 50
    val CLIPPING_THRESHOLD = 70

    // ogetto canvas
    // private var _canvas:Shape;
    // fattore di decellerazione
    val FACTOR = 0.05

    // valori visualizzati
    private var _microfoneLeftValue = 0.0
    private var _microfoneRightValue = 0.0

    // read values
    private var microfoneReadLeftValue = 0.0
    private var microfoneReadRightValue = 0.0
    fun setNoiseThreshold(noiseThreshold: Double) {
        NOISE_THRESHOLD = noiseThreshold.toInt()
    }

    /**
     * Sets the read decibels values (for stereo)
     */
    fun setValues(leftValue: Double, rightValue: Double) {
        microfoneReadLeftValue = leftValue
        microfoneReadRightValue = rightValue
    }

    public override fun onDraw(canvas: Canvas) {
        Log.i("MicrophoneVolumePicker", "Creating view")
        _canvasWidth = canvas.width
        _canvasHeight = canvas.height
        Log.i(
            "MicrophoneVolumePicker", "CanvasWidth: " + _canvasWidth
                    + " CanvasHeight: " + _canvasHeight
        )
        _drawableAreaHeight = _canvasHeight - PADDING_TOP - PADDING_BOTTOM
        _drawableAreaWidth = _canvasWidth - PADDING_LEFT - PADDING_RIGHT
        if (_isMono) _microfoneLeftValue = _microfoneRightValue
        _microfoneLeftValue = microfoneReadLeftValue
        _microfoneRightValue = microfoneReadRightValue

        // DECELLAROZIONE PER SMOOTHING DEI DATI IN INGRESSO
        _microfoneLeftValue =
            if (microfoneReadLeftValue < _microfoneLeftValue) // SE VALORE RILEVATO E' MINORE APPLICA DECELLERAZIONE
                microfoneReadLeftValue * FACTOR + _microfoneLeftValue * (1 - FACTOR) else  // ALTRIMENTI VAI AL VALORE APPENA RILEVATO
                microfoneReadLeftValue
        _microfoneRightValue =
            if (microfoneReadRightValue < _microfoneRightValue) microfoneReadRightValue * FACTOR + _microfoneRightValue * (1 - FACTOR) else microfoneReadRightValue

        // QUI EVENTUALI PROCEDURE DI QUANTIZZAZIONE E NORMALIZZAZIONE
        // NELL'INTERVALLO 0-120 del segnale
        val colomnWidth = 40
        // DRAW LEFT CHANNEL
        var originX = PADDING_LEFT + _drawableAreaWidth / 2 - 50
        drawVolumeRect(canvas, originX, colomnWidth, _microfoneLeftValue)

        // DRAWING RIGHT CHANNEL
        originX = PADDING_LEFT + _drawableAreaWidth / 2 + 50
        drawVolumeRect(canvas, originX, colomnWidth, _microfoneRightValue)

        // draw plot
        drawPlot(canvas, colomnWidth)
    }

    private fun drawVolumeRect(
        canvas: Canvas,
        originX: Int,
        width: Int,
        value: Double
    ) {
        val oneDbHeight = _drawableAreaHeight / FULL_SCALE.toDouble()
        var top = 0
        var bottom = 0
        // draws red rectangle: when sampled audio is greater than clipping threshold
        if (value > CLIPPING_THRESHOLD) {
            paint.color = Color.RED
            // determines the starting y-point for red area 
            bottom = (PADDING_TOP + (oneDbHeight * (FULL_SCALE - CLIPPING_THRESHOLD)) as Double).roundToInt()
            // determines the ending y-point for red area
            top = PADDING_TOP + (oneDbHeight * (FULL_SCALE - value)).toInt()
            canvas.drawRect(Rect(originX, top, originX + width, bottom), paint)
        }
        // draws orange rectangle: when sampled audio is greater than noise threshold
        if (value > NOISE_THRESHOLD) {
            paint.color = Color.rgb(0xFF, 0x80, 0x00)
            // determines the starting y-point for orange area 
            bottom = (PADDING_TOP + (oneDbHeight * (FULL_SCALE - NOISE_THRESHOLD)) as Double).roundToInt()
            // determines the ending y-point for orange area:
            // if the sampled value is lower than clipping threshold then the value is the 
            // ending point otherwise the starting point of the red area is the ending point 
            top = PADDING_TOP + (oneDbHeight * (FULL_SCALE - Math.min(
                value,
                CLIPPING_THRESHOLD.toDouble()
            ))).toInt()
            canvas.drawRect(Rect(originX, top, originX + width, bottom), paint)
        }

        // draws green rectangle: there must be something under noise threshold
        paint.color = Color.GREEN
        bottom = (PADDING_TOP + (oneDbHeight * FULL_SCALE)).roundToInt()
        top = PADDING_TOP + (oneDbHeight * (FULL_SCALE - Math.min(
            value,
            NOISE_THRESHOLD.toDouble()
        ))).toInt()
        canvas.drawRect(Rect(originX, top, originX + width, bottom), paint)
    }

    private fun drawPlot(canvas: Canvas, width: Int) {
        val step = 10 * (_drawableAreaHeight / FULL_SCALE.toFloat())
        var lineLevel = PADDING_TOP + _drawableAreaHeight.toFloat()
        val left = PADDING_LEFT + _drawableAreaWidth / 2 - 60.toFloat()
        val right = PADDING_LEFT + _drawableAreaWidth / 2 + 100.toFloat()
        for (i in 0 until FULL_SCALE / 10 + 1) {
            paint.color = Color.BLACK
            if (i * 10 == NOISE_THRESHOLD || i * 10 == CLIPPING_THRESHOLD) {
                paint.strokeWidth = 3.0f
                canvas.drawLine(left, lineLevel, right, lineLevel, paint)
                paint.strokeWidth = 0f
            } else {
                canvas.drawLine(left, lineLevel, right, lineLevel, paint)
            }
            if (i % 2 == 0) {
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText(Integer.toString(i * 10), left - 10, lineLevel, paint)
            } else {
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText(Integer.toString(i * 10), right + 10, lineLevel, paint)
            }
            lineLevel = lineLevel - step
        }
    }

    init {
        paint.textSize = 19f
    }
}