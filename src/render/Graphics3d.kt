package render

import javafx.geometry.Point3D
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.*
import kotlin.math.acos
import kotlin.math.sin

data class Point3DFloat(val x: Float, val y: Float, val z: Float)

fun Point3D.toFloat(): Point3DFloat {
    return Point3DFloat(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
}

class Graphics3d() {
    val DEBUG = true

    var points = ArrayList<Point3D>()
    var widths = ArrayList<Double>()
    var normals = ArrayList<Point3D>()
    //var lineColors =


    init {
    }

    fun moveTo(x: Double, y: Double, z: Double, normal: Point3D, width:Double, color: Color) {
        points.add(Point3D(x, y, z))
        widths.add(width)
        normals.add(normal)
    }

    fun lineTo(x: Double, y: Double, z: Double, normal: Point3D, width:Double, color: Color) {

        points.add(Point3D(x, y, z))
        widths.add(width)
        normals.add(normal)

    }

    fun meshView(): MeshView {
        val result = MeshView()
        val triangleMesh = TriangleMesh()
        result.mesh = triangleMesh

        triangleMesh.texCoords.addAll(0f, 0f)

        val material = PhongMaterial()
        material.diffuseColor = Color.BLUE
        result.drawMode = DrawMode.FILL
        result.material = material
        result.cullFace = CullFace.NONE

        if (points.size > 1) { //if you don't have at least 2 points, there are no lines...
            var thisSegmentNormal = Point3D(0.0, 0.0, 0.0)
            var nextSegmentNormal = Point3D(0.0, 0.0, 0.0)
            var thisPoint = Point3D(0.0, 0.0, 0.0)
            var previousPoint = Point3D(0.0, 0.0, 0.0)
            var nextPoint = Point3D(0.0, 0.0, 0.0)
            var offset = Point3D(0.0, 0.0, 0.0)
            var counterClockwise = true

            //for each point, calculate the offsets
            points.forEachIndexed({ i, currentPoint ->
                //if this is the first or last point, offsets are simply the cross product of the line segment and the line normal
                //we are not handling closed polygons here. there will be a Polygon class for that
                thisPoint = points[i]
                when (i) {
                    0 -> {
                        nextPoint = points[1]
                        thisSegmentNormal = nextPoint.subtract(thisPoint).normalize()
                        offset = thisSegmentNormal.crossProduct(normals[0]).multiply(widths[0] / 2.0)
                        if (DEBUG) println("endOffsets: $offset")
                    }
                    points.size - 1 -> {
                        previousPoint = points[i - 1]
                        thisSegmentNormal = thisPoint.subtract(previousPoint).normalize()
                        offset = thisSegmentNormal.crossProduct(normals[i]).multiply(widths[0] / 2.0)
                        if (DEBUG) println("endOffsets: $offset")
                    }
                    else -> {
                        offset = calcOffset(i)
                        if (DEBUG) println("endOffsets: $offset")
                    }
                }

                triangleMesh.points.addAll((thisPoint.x + offset.x).toFloat(), (thisPoint.y + offset.y).toFloat(), (thisPoint.z + offset.z).toFloat())  //v2
                triangleMesh.points.addAll(thisPoint.x.toFloat(), thisPoint.y.toFloat(), thisPoint.z.toFloat())                                         //v1
                triangleMesh.points.addAll((thisPoint.x - offset.x).toFloat(), (thisPoint.y - offset.y).toFloat(), (thisPoint.z - offset.z).toFloat())  //v0

                if (i > 0) {
                    //add faces
                    val pointOffset = (i - 1) * 3
                    if (counterClockwise) {
                        triangleMesh.faces.addAll(
                                pointOffset + 0, 0, pointOffset + 4, 0, pointOffset + 3, 0, //alternate zeroes are the placeholder for texture coords
                                pointOffset + 1, 0, pointOffset + 4, 0, pointOffset + 0, 0,
                                pointOffset + 1, 0, pointOffset + 5, 0, pointOffset + 4, 0,
                                pointOffset + 1, 0, pointOffset + 2, 0, pointOffset + 5, 0)
                    } else {
                        triangleMesh.faces.addAll(
                                pointOffset + 0, 0, pointOffset + 1, 0, pointOffset + 3, 0, //alternate zeroes are the placeholder for texture coords
                                pointOffset + 1, 0, pointOffset + 4, 0, pointOffset + 3, 0,
                                pointOffset + 0, 0, pointOffset + 3, 0, pointOffset + 5, 0,
                                pointOffset + 0, 0, pointOffset + 5, 0, pointOffset + 2, 0)
                    }

                }
            })

        }
        if (DEBUG) {
            for (i in 0 until triangleMesh.points.size() step 3) {
                println("[$i] (${triangleMesh.points[i]}, ${triangleMesh.points[i+1]}, ${triangleMesh.points[i+2]})")
            }

            for (i in 0 until triangleMesh.faces.size() step 6) {
                println("[$i] (${triangleMesh.faces[i]}, ${triangleMesh.faces[i+1]}, ${triangleMesh.faces[i+2]}, " +
                        "${triangleMesh.faces[i+3]}, ${triangleMesh.faces[i+4]}, ${triangleMesh.faces[i+5]})")
            }
        }
        return result
    }

    private fun calcOffset(i: Int): Point3D {
        val v1 = points[i - 1].subtract(points[i])
        val v2 = points[i + 1].subtract(points[i])
        val bisectingAngle = bisectingAngle(v1, v2)
        val bisectingUnitVector = bisectingUnitVector(v1, v2)
        val distance = (widths[i] / 2.0) * (1 / sin(bisectingAngle))
        val point = bisectingUnitVector.multiply(distance)
        return point.multiply(-1.0)

    }

    private fun bisectingAngle(p1: Point3D, p2: Point3D): Double {
        val numerator = p1.x * p2.x + p1.y * p2.y + p1.z * p2.z
        val denominator = p1.magnitude() * p2.magnitude()
        return (acos(numerator / denominator))/2
    }

    private fun bisectingUnitVector(p1: Point3D, p2: Point3D): Point3D {
        val n1 = p1.normalize()
        val n2 = p2.normalize()
        return Point3D((n1.x + n2.x)/2, (n1.y + n2.y)/2, (n1.z + n2.z)/2)
    }


}
