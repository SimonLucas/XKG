package test

import gui.*
import gui.layout.LRect
import math.Vec2d
import org.w3c.dom.*
import kotlin.math.PI
import kotlin.math.min

class XGraphicsJS(var canvas: HTMLCanvasElement) : XGraphics {

    val drawList = ArrayList<Drawable>()
    // val c = canvas

//    override fun width() = canvas.width.toDouble()
//    override fun height() = canvas.height.toDouble()

    var rect: LRect? = null
    override fun setBounds(rect: LRect) {
        this.rect = rect

    }

    var translate = Vec2d()

    override fun setTranslate(x: Double, y: Double) {
        translate += Vec2d(x, y)
    }

    override fun releaseBounds() {
        rect = null
    }

    override fun width(): Double {
        val bounds = rect
        return if (bounds != null) bounds.width else canvas.width.toDouble()
    }

    override fun height(): Double {
        val bounds = rect
        return if (bounds != null) bounds.height else canvas.height.toDouble()
    }


    override fun draw(toDraw: Drawable) {

        val context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.save()

        // apply clipping bounds if necessary
        val r = rect
        r?.let {
            context.translate(r.xLeft, r.yTop)
            val path2D = Path2D()
            path2D.rect(0.0, 0.0, r.width, r.height)
            context.clip(path2D)
        }
        context.translate(translate.x, translate.y)

        if (toDraw is XRect) drawRect(toDraw)
        if (toDraw is XRoundedRect) drawRoundedRect(toDraw)
        if (toDraw is XEllipse) drawEllipse(toDraw)
        if (toDraw is XPoly) drawPoly(toDraw)
        if (toDraw is XLine) drawLine(toDraw)
        if (toDraw is XText) drawText(toDraw)

        context.restore()
    }

    fun drawRect(rect: XRect) {
        val g = canvas
        with(rect) {
            with(rect.dStyle) {
                val context = g.getContext("2d") as CanvasRenderingContext2D
                context.save()
                context.globalAlpha = 1.0;
                context.translate(rect.centre.x, rect.centre.y)
                context.rotate(rect.rotation)

                if (fill) {
                    context.fillStyle = rgba(fg)
//                    context.fillRect(centre.x - w / 2, centre.y - h / 2, w, h)
                    context.fillRect(-w / 2, -h / 2, w, h)
                }
                if (stroke) {
                    context.strokeStyle = rgba(lc)
                    context.lineWidth = lineWidth
//                    context.strokeRect(centre.x - w / 2, centre.y - h / 2, w, h)
                    context.strokeRect(-w / 2, -h / 2, w, h)

                }
                context.restore()
            }
        }
    }


    fun drawRoundedRect(rect: XRoundedRect) {
        val g = canvas
        with(rect) {
            with(rect.dStyle) {
                val context = g.getContext("2d") as CanvasRenderingContext2D
                context.save()
                context.globalAlpha = 1.0;
                context.translate(rect.centre.x, rect.centre.y)
                context.rotate(rect.rotation)

                val rad =
                    if (radInPercent) min(w, h) * cornerRad
                    else cornerRad

                with(context) {
                    roundRectPath(context, w, h, rad)
                    if (fill) {
                        context.fillStyle = rgba(fg)
                        context.fill()
                    }
                    if (stroke) {
                        context.strokeStyle = rgba(lc)
                        context.lineWidth = lineWidth
                        context.stroke()
                    }
                    context.restore()
                }
            }
        }
    }

    fun roundRectPath(context: CanvasRenderingContext2D, w: Double, h: Double, rad: Double) {
        var r = rad
        val x = -w / 2
        val y = -h / 2
        with(context) {
            if (w < 2 * r) r = w / 2;
            if (h < 2 * r) r = h / 2;
            beginPath();
            moveTo(x + r, y);
            arcTo(x + w, y, x + w, y + h, r);
            arcTo(x + w, y + h, x, y + h, r);
            arcTo(x, y + h, x, y, r);
            arcTo(x, y, x + w, y, r);
            this.closePath();
        }
    }

    fun roundRectPathOld(context: CanvasRenderingContext2D, w: Double, h: Double, rad: Double) {
        var r = rad
        val x = -w / 2
        val y = -h / 2
        with(context) {
            if (w < 2 * r) r = w / 2;
            if (h < 2 * r) r = h / 2;
            beginPath()
            moveTo(x + r, y)
            lineTo(x + w - r, y)
            quadraticCurveTo(x + w, y, x + w, y + r)
            lineTo(x + w, y + h - r)
            quadraticCurveTo(x + w, y + h, x + w - r, y + h)
            lineTo(x + r, y + h)
            quadraticCurveTo(x, y + h, x, y + h - r)
            lineTo(x, y + r)
            quadraticCurveTo(x, y, x + r, y)
            closePath()
        }
    }

    fun drawEllipse(ellipse: XEllipse) {
        // this is not working yet
        val g = canvas
        with(ellipse) {
            with(ellipse.dStyle) {
                val context = g.getContext("2d") as CanvasRenderingContext2D
                context.save()
                context.globalAlpha = 1.0;
                context.translate(ellipse.centre.x, ellipse.centre.y)
                context.rotate(ellipse.rotation)

                // note: must call beginPath before adding the ellipse
                context.beginPath()
                context.ellipse(0.0, 0.0, w / 2, h / 2, 0.0, 0.0, PI * 2, true)

                if (fill) {
                    context.fillStyle = rgba(fg)
                    context.fill()
                }
                if (stroke) {
                    context.strokeStyle = rgba(lc)
                    context.lineWidth = lineWidth
                    context.stroke()
                }
                context.restore()
            }
        }
    }

    fun drawLine(line: XLine) {
        val g = canvas
        with(line) {
            val path = Path2D()
            path.moveTo(a.x, a.y)
            path.lineTo(b.x, b.y)
            path.closePath()
            with(line.dStyle) {
                val context = g.getContext("2d") as CanvasRenderingContext2D
                context.save()
                if (stroke) {
                    context.strokeStyle = rgba(lc)
                    context.lineWidth = lineWidth
                    context.stroke(path)
                }
                context.restore()
            }
        }
    }

    fun drawText(text: XText) {
        val g = canvas
        if (g != null) {
            with(text) {
                with(tStyle) {
                    val context = g.getContext("2d") as CanvasRenderingContext2D
                    context.save()
                    context.font = "${size.toInt()}px Arial";
                    context.fillStyle = rgba(fg)
                    // context.strokeStyle = rgba(fg)
                    // println("Set style: ${rgba(fg)} for ${text.str}")
                    context.textAlign = CanvasTextAlign.CENTER
                    val metrics = context.measureText(str)
                    context.fillText(str, p.x, p.y + metrics.actualBoundingBoxAscent / 2)
                    context.restore()
                }
            }
        }
    }

    fun v(x: Float) = (255 * x).toInt()

    fun rgba(xColor: XColor): String {
        return "rgba(${v(xColor.r)}, ${v(xColor.g)}, ${v(xColor.b)}, ${xColor.a})"
    }

    fun drawPoly(poly: XPoly) {
        val path = Path2D()
        with(poly) {
            path.moveTo(points[0].x, points[0].y)
            for (v in points) path.lineTo(v.x, v.y)

            if (poly.closed) path.closePath()
        }
        val g = canvas
        if (g != null) {
            with(poly.dStyle) {
                val context = g.getContext("2d") as CanvasRenderingContext2D
                context.save();
                // context.beginPath();
                context.lineWidth = lineWidth

                // fg.b = 0.5
                context.translate(poly.centre.x, poly.centre.y)
                context.rotate(poly.rotation)
                context.fillStyle = rgba(fg)

                // context.moveTo(poly.start.x, poly.start.y)
                // println("Moving to ${poly.start}")
                if (fill)
                    context.fill(path)

                // context.moveTo(poly.start.x, poly.start.y)
                if (stroke)
                    context.strokeStyle = rgba(lc)

                context.stroke(path)
                context.restore();
            }
        }
    }


    override fun drawables(): ArrayList<Drawable> {
        return drawList
    }

    override fun redraw() {
        TODO("Not yet implemented")
    }

    var intStyle = XStyle()

    override var style: XStyle
        get() = intStyle
        set(value) {
            intStyle = value
        }
}