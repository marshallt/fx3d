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
import javafx.scene.transform.Rotate
import render.Graphics3d
import render.View3d


class Main : Application() {
    //@Throws(Exception::class)

    val root = Group()
    val scene = View3d(root, 1200.0, 900.0, true, SceneAntialiasing.BALANCED)
    var camera = PerspectiveCamera(true)


    override fun start(primaryStage: Stage) {
        var box = Box(100.0, 100.0, 100.0)
        var material = PhongMaterial(Color.RED)
        box.material = material

        
        scene.fill = Color.GRAY

        val g = Graphics3d()
        g.moveTo(-100.0, 0.0, -51.0)
        g.lineTo(100.0, 0.0, -51.0, Point3D(0.0, 0.0, 0.0), 5.0, Color.BLUE)
        g.lineTo(150.0, -20.0, -51.0, Point3D(0.0, 0.0, 0.0), 5.0, Color.BLUE)
        g.lineTo(20.0, 200.0, -51.0, Point3D(0.0, 0.0, 0.0), 5.0, Color.BLUE)
        g.lineTo(100.0, 100.0, -51.0, Point3D(0.0, 0.0, 0.0), 5.0, Color.BLUE)

        root.children.addAll(box, g.meshView())


        camera.nearClip = 0.1
        camera.farClip = 10000.0
        camera.translateZ = -400.0
        camera.translateY = 0.0
        camera.translateX = 0.0
        camera.fieldOfView = 45.0

        scene.camera = camera


        primaryStage.scene = scene
        primaryStage.show()

    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}