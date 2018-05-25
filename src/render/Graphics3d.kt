package render

import javafx.geometry.Point3D
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.*

data class Point3DFloat(val x: Float, val y: Float, val z: Float)

fun Point3D.toFloat(): Point3DFloat {
    return Point3DFloat(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
}

class Graphics3d() {

    var linePoints = ArrayList<Point3D>()
    var lineEndWidths = ArrayList<Double>()
    var lineNormals = ArrayList<Point3D>()
    //var lineColors =


    init {
    }

    fun moveTo(x: Double, y: Double, z: Double) {
        linePoints.add(Point3D(x, y, z))
    }

    fun lineTo(x: Double, y: Double, z: Double, normal: Point3D, endWidth:Double, color: Color) {

        linePoints.add(Point3D(x, y, z))
        lineEndWidths.add(endWidth)
        lineNormals.add(normal)

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

        if (linePoints.size > 1) { //if you don't have at least 2 points, there are no lines...


            //ignore normals for now and make the line vertical (fixed z)
            //add the first point and offsets to the mesh
            //TODO: use correct offsets when line not horizontal
            var fromPoint = linePoints[0]
            var endOffsets = Vector3d(0.0,0.0,0.0)
            triangleMesh.points.addAll(fromPoint.x.toFloat(), fromPoint.y.toFloat(), fromPoint.z.toFloat())                //v0
            triangleMesh.points.addAll(fromPoint.x.toFloat(), (fromPoint.y - endOffset).toFloat(), fromPoint.z.toFloat())  //v1
            triangleMesh.points.addAll(fromPoint.x.toFloat(), (fromPoint.y + endOffset).toFloat(), fromPoint.z.toFloat())  //v2

            linePoints.drop(1).forEachIndexed({ i, toPoint ->
                endOffset = lineEndWidths[i] / 2.0
                triangleMesh.points.addAll(toPoint.x.toFloat(), toPoint.y.toFloat(), toPoint.z.toFloat())                   //v3
                triangleMesh.points.addAll(toPoint.x.toFloat(), (toPoint.y - endOffset).toFloat(), toPoint.z.toFloat())     //v4
                triangleMesh.points.addAll(toPoint.x.toFloat(), (toPoint.y + endOffset).toFloat(), toPoint.z.toFloat())     //v5

                //add faces
                val pointOffset = i * 3
                if (fromPoint.x <= toPoint.x) {
                    triangleMesh.faces.addAll(
                            pointOffset + 0, 0, pointOffset + 4, 0, pointOffset + 1, 0, //alternate zeroes are the placeholder for texture coords
                            pointOffset + 3, 0, pointOffset + 2, 0, pointOffset + 5, 0,
                            pointOffset + 0, 0, pointOffset + 2, 0, pointOffset + 3, 0,
                            pointOffset + 0, 0, pointOffset + 3, 0, pointOffset + 4, 0)
                } else {
                    triangleMesh.faces.addAll(
                            pointOffset + 0, 0, pointOffset + 1, 0, pointOffset + 3, 0, //alternate zeroes are the placeholder for texture coords
                            pointOffset + 1, 0, pointOffset + 4, 0, pointOffset + 3, 0,
                            pointOffset + 0, 0, pointOffset + 3, 0, pointOffset + 5, 0,
                            pointOffset + 0, 0, pointOffset + 5, 0, pointOffset + 2, 0)
                }

                fromPoint = linePoints[i+1]
            })
        }
        return result
    }


}
