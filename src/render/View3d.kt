package render

import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.scene.SceneAntialiasing
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.transform.Rotate

class View3d(root: Parent, width: Double, height: Double, depthBuffer: Boolean, antiAliasing: SceneAntialiasing):
        Scene(root, width, height, depthBuffer, antiAliasing) {

    var mousePosX = 0.0
    var mousePosY = 0.0
    var mouseOldX = 0.0
    var mouseOldY = 0.0
    var rotateX = Rotate(0.0, Rotate.X_AXIS)
    var rotateY = Rotate(0.0, Rotate.Y_AXIS)
    var cam = PerspectiveCamera(true)

    init {
        root.transforms.addAll(rotateX, rotateY)
        handleMouseEvents()
        initCamera()
    }

    fun  initCamera() {
        cam.nearClip = 0.1
        cam.farClip = 10000.0
        cam.translateZ = -400.0
        cam.translateY = 0.0
        cam.translateX = 0.0
        cam.fieldOfView = 45.0
        this.camera = cam

    }

    private fun handleMouseEvents() {
        this.onMousePressed = EventHandler<MouseEvent> { me: MouseEvent ->
            mousePosX = me.sceneX
            mousePosY = me.sceneY
            mouseOldX = me.sceneX
            mouseOldY = me.sceneY
        }

        this.onMouseDragged = EventHandler<MouseEvent> { me: MouseEvent ->
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

        this.onScroll = EventHandler<ScrollEvent> { me: ScrollEvent ->
            if (me.deltaY > 0) {
                camera.translateZ += 50.0
                if (camera.translateZ > 0.0) camera.translateZ = 0.0
            } else {
                camera.translateZ -= 50
                if (camera.translateZ < -1000.0) camera.translateZ = -1000.0

            }
            println(camera.translateZ)
        }
    }

}