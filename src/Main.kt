import javafx.application.Application
import javafx.geometry.Point3D
import javafx.scene.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Box
import render.Graphics3d
import render.View3d


class Main : Application() {
    //@Throws(Exception::class)

    val root = Group()
    val scene = View3d(root, 1200.0, 900.0, true, SceneAntialiasing.BALANCED)


    override fun start(primaryStage: Stage) {

        drawScene()
        primaryStage.scene = scene
        primaryStage.show()

    }

    fun drawScene() {
        scene.fill = Color.GRAY

        var box = Box(100.0, 100.0, 100.0)
        var material = PhongMaterial(Color.RED)
        box.material = material

        val g = Graphics3d()
        g.moveTo(0.0, 0.0, 0.0, Point3D(0.0, 0.0, 1.0), 20.0, Color.BLUE)
        g.lineTo(100.0, 40.0, 0.0, Point3D(0.0, 0.0, 1.0), 20.0, Color.BLUE)
        g.lineTo(100.0, 100.0, 0.0, Point3D(0.0, 0.0, 1.0), 20.0, Color.RED)
        //g.lineTo(-43.3, 25.0, -51.0, Point3D(0.0, 0.0, 1.0), 5.0, Color.BLUE)
        //g.lineTo(-43.3, -25.0, -51.0, Point3D(0.0, 0.0, 1.0), 5.0, Color.BLUE)
        //g.lineTo(0.0, -50.0, -51.0, Point3D(0.0, 0.0, 1.0), 5.0, Color.BLUE)
        //g.lineTo(43.3, -25.0, -51.0, Point3D(0.0, 0.0, 1.0), 5.0, Color.BLUE)



/*
        g.lineTo(100.0, 0.0, -51.0, Point3D(0.0, 0.0, 1.0), 5.0, Color.BLUE)
        g.lineTo(150.0, -20.0, -51.0, Point3D(0.0, 0.0, 1.0), 10.0, Color.BLUE)
        g.lineTo(20.0, 200.0, -51.0, Point3D(0.0, 0.0, 0.0), 5.0, Color.BLUE)
        g.lineTo(100.0, 100.0, -51.0, Point3D(0.0, 0.0, 0.0), 5.0, Color.BLUE)
*/
        root.children.addAll(g.meshView())

    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}