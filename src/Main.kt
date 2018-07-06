import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Point3D
import javafx.scene.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Box
import javafx.scene.shape.Cylinder
import javafx.scene.shape.Sphere
import javafx.scene.transform.Rotate
import javafx.scene.transform.Translate




class Main : Application() {
    //@Throws(Exception::class)

    var mousePosX = 0.0
    var mousePosY = 0.0
    var mouseOldX = 0.0
    var mouseOldY = 0.0
    var rotateX = Rotate(25.0, Rotate.X_AXIS)
    var rotateY = Rotate(25.0, Rotate.Y_AXIS)

    lateinit var scene: Scene
    var camera = PerspectiveCamera(true)


    override fun start(primaryStage: Stage) {
        val root = Group()
        root.transforms.addAll(rotateX, rotateY)

        var sphere = Sphere(1.0)
        var material = PhongMaterial(Color.RED)
        sphere.material = material
        root.children.addAll(sphere)

        var l1 = line(Point3D(-1.0, -1.1, 0.0), Point3D(1.0, -1.1, 0.0), 0.001)
        l1.material = PhongMaterial(Color.BLUE)
        root.children.addAll(l1)

        var l2 = line(Point3D(1.0, -1.1, 0.0), Point3D(1.25, -0.1, -1.0), 0.001)
        l2.material = PhongMaterial(Color.BLUE)
        root.children.addAll(l2)

        var disk = line(Point3D(-0.0001, 0.0, 0.0), Point3D(0.0001, 0.0, 0.0), 2.0)
        disk.material = PhongMaterial(Color.GREENYELLOW)
        root.children.addAll(disk)

        scene = Scene(root, 1200.0, 900.0, true, SceneAntialiasing.BALANCED)
        scene.fill = Color.LIGHTGRAY



        camera.nearClip = 0.1
        camera.farClip = 10000.0
        camera.translateZ = -4.0
        camera.translateY = 0.0
        camera.translateX = 0.0
        camera.fieldOfView = 45.0

        scene.camera = camera


        primaryStage.scene = scene
        primaryStage.show()
        handleMouseEvents()
    }


    private fun line(origin: Point3D, target: Point3D, radius: Double): Cylinder {
        val yAxis = Point3D(0.0, 1.0, 0.0)
        val diff = target.subtract(origin)
        val height = diff.magnitude()

        val mid = target.midpoint(origin)
        val moveToMidpoint = Translate(mid.getX(), mid.getY(), mid.getZ())

        val axisOfRotation = diff.crossProduct(yAxis)
        val angle = Math.acos(diff.normalize().dotProduct(yAxis))
        val rotateAroundCenter = Rotate(-Math.toDegrees(angle), axisOfRotation)

        val line = Cylinder(radius, height, 4)

        line.transforms.addAll(moveToMidpoint, rotateAroundCenter)

        return line
    }

    private fun handleMouseEvents() {
        scene.onMousePressed = EventHandler<MouseEvent> { me: MouseEvent ->
            mousePosX = me.sceneX
            mousePosY = me.sceneY
            mouseOldX = me.sceneX
            mouseOldY = me.sceneY
        }

        scene.onMouseDragged = EventHandler<MouseEvent > { me: MouseEvent ->
            mousePosX = me.sceneX
            mousePosY = me.sceneY
            val dx = mousePosX - mouseOldX
            val dy = mousePosY - mouseOldY
            mouseOldX = mousePosX
            mouseOldY = mousePosY
            if (me.isPrimaryButtonDown) {
                rotateY.setAngle(rotateY.getAngle() - dx)
                rotateX.setAngle(rotateX.getAngle() + dy)
            }
        }

        scene.onScroll = EventHandler<ScrollEvent> {
            if (it.deltaY > 0) {
                camera.translateZ += 0.50
                if (camera.translateZ > 0.0) camera.translateZ = 0.0
            } else {
                camera.translateZ -= 0.50
                if (camera.translateZ < -1000.0) camera.translateZ = -1000.0

            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}