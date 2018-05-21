import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.effect.Light
import javafx.scene.input.MouseEvent
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Box
import javafx.scene.shape.CubicCurve
import javafx.scene.transform.Rotate


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
        var box = Box(100.0, 100.0, 100.0)
        var material = PhongMaterial(Color.RED)
        box.material = material

        val root = Group()
        scene = Scene(root, 1200.0, 900.0, true, SceneAntialiasing.BALANCED)
        scene.fill = Color.GRAY

        root.children.addAll(box)


        camera.nearClip = 0.1
        camera.farClip = 10000.0
        camera.translateZ = -400.0
        camera.translateY = 0.0
        camera.translateX = 0.0
        camera.fieldOfView = 45.0

        scene.camera = camera


        primaryStage.scene = scene
        primaryStage.show()
        handleMouseEvents()
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
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}