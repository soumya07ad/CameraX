package com.example.camerax
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.camerax.ui.theme.CameraxTheme
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : ComponentActivity() {

  private var recording : Recording? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasrequiredPermissions()){
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSION,0
            )
        }
        enableEdgeToEdge()
        setContent {
            CameraxTheme {
                val scope = rememberCoroutineScope()
               val scaffoldState = rememberBottomSheetScaffoldState()
                val controller = remember{
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or
                                    CameraController.VIDEO_CAPTURE
                        )
                    }
                }
                val viewModel = viewModel<MainViewModel>()
                val bitmaps by viewModel.bitmaps.collectAsState()
                BottomSheetScaffold(
                    scaffoldState=scaffoldState,
                    sheetPeekHeight = 0.dp,
                    sheetContent =
                        {
                       PhotoBottomSheet(
                           bitmaps = bitmaps,
                           modifier = Modifier
                               .fillMaxWidth()
                       )
                    }
                ) { padding ->
                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    )
                    {
                      CameraPreview(
                          controller = controller,
                          modifier = Modifier
                              .fillMaxSize()
                      )
                        IconButton(
                            onClick = {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    }else{
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                            },
                                    modifier = Modifier.offset(16.dp,16.dp)



                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch Camera"
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(
                                onClick = {
                               scope.launch {
                                   scaffoldState.bottomSheetState.expand()
                               }
                                }
                            ){
                                Icon(
                                    imageVector = Icons.Default.Cameraswitch,
                                    contentDescription = "open gallery"
                                )
                            }
                            IconButton(
                                onClick = {
                                 takePhoto(
                                     controller = controller,
                                     onPhotoTaken = viewModel::onTakePhoto
                                 )
                                }
                            ){
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Take photo"
                                )
                            }
                            IconButton(
                                onClick = {
                                    recordVideo(controller)
                                }
                            ){
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = "Record video"
                                )
                            }

                        }
                    }
                }

              }
            }
        }
    private fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit
    ){
        if (! hasrequiredPermissions()){
            return
        }
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : OnImageCapturedCallback(){
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply{
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
//                        postScale(
//                            1f,
//                            -1f,
//                        )
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera","couldn't photo: ",exception)
                }
            }
        )
    }
@SuppressLint("MissingPermission")
    private fun recordVideo(controller: LifecycleCameraController){
        if (recording != null){
            recording?.stop()
            recording = null
            return
        }
        if (! hasrequiredPermissions()){
            return
        }
        val outputFile = File(filesDir,"my-recording.mp4")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        recording = controller.startRecording(
            FileOutputOptions.Builder(outputFile).build(),
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(applicationContext),
        ){ event ->
            when(event){
                is VideoRecordEvent.Finalize -> {
                 if (event.hasError()){
                     recording?.close()
                     recording = null

                     Toast.makeText(
                         applicationContext,
                         "Video capture failed",
                         Toast.LENGTH_LONG
                     ).show()
                 }else{
                     Toast.makeText(
                         applicationContext,
                         "Video capture Succed",
                         Toast.LENGTH_LONG
                     ).show()

                 }

                }
            }

        }
    }

    private fun hasrequiredPermissions(): Boolean{
        return CAMERAX_PERMISSION.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
      companion object{
          private val CAMERAX_PERMISSION = arrayOf(
              android.Manifest.permission.CAMERA,
              android.Manifest.permission.RECORD_AUDIO,
          )
      }
   }


