package com.example.hexacopterapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.View
import androidx.annotation.Nullable


class HexaView : SurfaceView {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint_text = Paint(Paint.ANTI_ALIAS_FLAG)
    var angle_cren: Float = 15f
    var angle_tang: Float = 10f
    var angle_risk: Float = 225f
    val a = 1

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        var width = width.toFloat()
        var height = height.toFloat()


        canvas.scale(.5f * width, -1f * height)
        canvas.translate(1f, -1f)
        paint.color = Color.parseColor("#17C3E1")
        paint.style = Paint.Style.FILL
        canvas.drawRect(-1f, 0.5f, 1f, 1f, paint)
        paint.color = Color.parseColor("#FF7CFF46")
        canvas.drawRect(-1f, 0f, 1f, 0.5f, paint)

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.005f
        //Шкала для угла крена
        val maxValue = 150
        val value = 25
        val scale = 0.97f
        val step = Math.PI / 180
        for (i in 30..maxValue) {
            val x1 = Math.cos(step * i).toFloat() * 0.5f
            val y1 = Math.sin(step * i).toFloat() * 0.5f
            var x2: Float
            var y2: Float
            if (i % 10 == 0) {
                x2 = x1 * scale * 0.98f * 0.99f
                y2 = y1 * scale * 0.98f * 0.99f
            } else {
                x2 = x1 * scale * 0.99f
                y2 = y1 * scale * 0.99f
            }
            canvas.drawLine(x1, (y1 + 0.5f), x2, (y2 + 0.5f), paint)
        }
        //Шкала для угла рысканья
        for (i in 0..360) {
            val x1 = Math.cos(step * i).toFloat() * 0.15f
            val y1 = Math.sin(step * i).toFloat() * 0.15f
            var x2: Float
            var y2: Float
            if (i % 45 == 0){
                x2 = x1 * scale * 0.95f * 0.99f
                y2 = y1 * scale * 0.95f * 0.99f
            } else if (i % 10 == 0) {
                x2 = x1 * scale * 0.98f * 0.99f
                y2 = y1 * scale * 0.98f * 0.99f
            } else {
                x2 = x1 * scale * 0.99f
                y2 = y1 * scale * 0.99f
            }
            canvas.drawLine((x1 + 0.75f), (y1 + 0.75f), (x2 + 0.75f), (y2 + 0.75f), paint)
        }
        paint.strokeWidth = 0.0025f
        canvas.drawLine(0.75f, 0.6f, 0.75f, 0.9f, paint)
        canvas.drawLine(0.6f, 0.75f, 0.9f, 0.75f, paint)

        //Шкала для угла тангажа
        paint.color = Color.WHITE
        paint.strokeWidth = 0.005f
        canvas.drawLine(-0.1f, 0.1f, 0.1f, 0.1f, paint)
        canvas.drawLine(-0.05f, 0.2f, 0.05f, 0.2f, paint)
        canvas.drawLine(-0.1f, 0.3f, 0.1f, 0.3f, paint)
        canvas.drawLine(-0.05f, 0.4f, 0.05f, 0.4f, paint)
        canvas.drawLine(-0.1f, 0.5f, 0.1f, 0.5f, paint)
        canvas.drawLine(-0.05f, 0.6f, 0.05f, 0.6f, paint)
        canvas.drawLine(-0.1f, 0.7f, 0.1f, 0.7f, paint)
        canvas.drawLine(-0.05f, 0.8f, 0.05f, 0.8f, paint)
        canvas.drawLine(-0.1f, 0.9f, 0.1f, 0.9f, paint)

        //Указатель курса и угла тангажа
        paint.color = Color.RED
        paint.strokeWidth = 0.005f
        var h: Float = 0.2f / 10
        if (angle_tang > 20f) {
            angle_tang = 20f
        }
        canvas.drawLine(Math.cos(step * angle_cren).toFloat() * 0.45f, Math.sin(step * angle_cren).toFloat() * 0.45f + 0.5f + (h * angle_tang), Math.cos(step * angle_cren).toFloat() * 0.2f, Math.sin(step * angle_cren).toFloat() * 0.2f + 0.5f + (h * angle_tang), paint)
        canvas.drawLine(Math.cos(step * angle_cren).toFloat() * -0.45f, Math.sin(step * angle_cren).toFloat() * -0.45f + 0.5f + (h * angle_tang), Math.cos(step * angle_cren).toFloat() * -0.2f, Math.sin(step * angle_cren).toFloat() * -0.2f + 0.5f + (h * angle_tang), paint)
        canvas.drawLine(Math.cos(step * (angle_cren - 30)).toFloat() * 0.11f, Math.sin(step * (angle_cren - 30)).toFloat() * 0.11f + 0.5f + (h * angle_tang), 0f, 0.5f + (h * angle_tang), paint)
        canvas.drawLine(Math.cos(step * (angle_cren + 30)).toFloat() * -0.11f, Math.sin(step * (angle_cren + 30)).toFloat() * -0.11f + 0.5f + (h * angle_tang), 0f, 0.5f + (h * angle_tang), paint)
        paint.strokeWidth = 0.005f

        //Указатель угла крена
        canvas.drawLine(Math.cos(step * (angle_cren + 90f)).toFloat() * 0.4851f, Math.sin(step * (angle_cren + 90f)).toFloat() * 0.4851f + 0.5f, Math.cos(step * (angle_cren + 90f + 3f)).toFloat() * 0.4851f * 0.93f, Math.sin(step * (angle_cren + 90f + 3f)).toFloat() * 0.4851f * 0.93f + 0.5f, paint)
        canvas.drawLine(Math.cos(step * (angle_cren + 90f)).toFloat() * 0.4851f, Math.sin(step * (angle_cren + 90f)).toFloat() * 0.4851f + 0.5f, Math.cos(step * (angle_cren + 90f - 3f)).toFloat() * 0.4851f * 0.93f, Math.sin(step * (angle_cren + 90f - 3f)).toFloat() * 0.4851f * 0.93f + 0.5f, paint)
        canvas.drawLine(Math.cos(step * (angle_cren + 90f + 3f)).toFloat() * 0.4851f * 0.93f, Math.sin(step * (angle_cren + 90f + 3f)).toFloat() * 0.4851f * 0.93f + 0.5f, Math.cos(step * (angle_cren + 90f - 3f)).toFloat() * 0.4851f * 0.93f, Math.sin(step * (angle_cren + 90f - 3f)).toFloat() * 0.4851f * 0.93f + 0.5f, paint)

        //Указатель угла рысканья
        paint.strokeWidth = 0.0025f

        canvas.drawLine(Math.cos(step * angle_risk).toFloat() * -0.01f + 0.75f, Math.sin(step * angle_risk).toFloat() * 0.01f + 0.75f, Math.cos(step * angle_risk).toFloat() * 0.01f + 0.75f,Math.sin(step * angle_risk).toFloat() * -0.01f + 0.75f, paint)
        canvas.drawLine(Math.cos(step * angle_risk).toFloat() * -0.01f + 0.75f, Math.sin(step * angle_risk).toFloat() * 0.01f + 0.75f, Math.cos(step * (angle_risk - 90)).toFloat() * 0.15f + 0.75f,Math.sin(step * (angle_risk + 90)).toFloat() * 0.15f + 0.75f, paint)
        canvas.drawLine(Math.cos(step * angle_risk).toFloat() * 0.01f + 0.75f,Math.sin(step * angle_risk).toFloat() * -0.01f + 0.75f, Math.cos(step * (angle_risk - 90)).toFloat() * 0.15f + 0.75f,Math.sin(step * (angle_risk + 90)).toFloat() * 0.15f + 0.75f, paint)

        canvas.save()
        canvas.rotate(90 - 180.toFloat() * (value / maxValue.toFloat()))
        canvas.restore()
        canvas.restore()
    }
}