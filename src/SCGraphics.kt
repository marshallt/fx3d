import javafx.geometry.Point3D
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.*

data class Point3DFloat(val x: Float, val y: Float, val z: Float)

fun Point3D.toFloat(): Point3DFloat {
    return Point3DFloat(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
}

class ScGraphics(): MeshView() {

    private var currentX = 0.0
    private var currentY = 0.0
    private var currentZ = 0.0

    var triangleMesh = TriangleMesh()


    init {
        triangleMesh.texCoords.addAll(0f, 0f)
        val material = PhongMaterial()
        material.diffuseColor = Color.BLUE
        this.mesh = triangleMesh
        this.drawMode = DrawMode.FILL
        this.material = material
        this.cullFace = CullFace.NONE
    }

    fun moveTo(x: Double, y: Double, z: Double) {
        currentX = x
        currentY = y
        currentZ = z
    }

    fun lineTo(point: Point3D, normal: Point3D, width:Double, color: Color) {

        val pointOffset = triangleMesh.points.size()
        val offsetY = width/2.0



        triangleMesh.points.addAll(currentX.toFloat(), currentY.toFloat(), currentZ.toFloat())             //v0
        triangleMesh.points.addAll(point.x.toFloat(), point.y.toFloat(), point.z.toFloat())                //v1
        triangleMesh.points.addAll(point.x.toFloat(), (point.y + offsetY).toFloat(), point.z.toFloat())    //v2
        triangleMesh.points.addAll(currentX.toFloat(), (currentY + offsetY).toFloat(), currentZ.toFloat()) //v3
        triangleMesh.points.addAll(point.x.toFloat(), (point.y - offsetY).toFloat(), point.z.toFloat())    //v4
        triangleMesh.points.addAll(currentX.toFloat(), (currentY - offsetY).toFloat(), point.z.toFloat())  //v5

        //add dummy texture coordinate

        //add faces
        triangleMesh.faces.addAll(
                pointOffset + 0, 0, pointOffset + 3, 0, pointOffset + 1, 0, //alternate zeroes are the placeholder for texture coords
                pointOffset + 2, 0, pointOffset + 1, 0, pointOffset + 3, 0,
                pointOffset + 0, 0, pointOffset + 1, 0, pointOffset + 4, 0,
                pointOffset + 0, 0, pointOffset + 4, 0, pointOffset + 5, 0)

    }

    fun mesh(): TriangleMesh {
        return triangleMesh
    }
}
