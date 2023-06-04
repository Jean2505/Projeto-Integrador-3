package br.edu.puccampinas.pi3

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import br.edu.puccampinas.pi3.databinding.ActivityCameraPreviewBinding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector

    private var imageCapture: ImageCapture? = null

    private lateinit var imgCaptureExecutor: ExecutorService
    //val intentX = Intent(this, CurriculoActivity::class.java)
    private lateinit var intentX: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        startCamera()

        binding.btnTakePhoto.setOnClickListener {
            intentX = Intent(this, CurriculoActivity::class.java)
            val foto = takePhoto()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                blinkPreview()
            }
            Toast.makeText(this, "Salvando foto", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                intentX.putExtra("fotoPerfil", foto)
                intentX.putExtra("IEmail", intent.getStringExtra("IEmail").toString())
                intentX.putExtra("INome", intent.getStringExtra("INome"))
                intentX.putExtra("ISenha", intent.getStringExtra("ISenha").toString())
                intentX.putExtra("ITelefone", intent.getStringExtra("ITelefone"))
                intentX.putExtra("IEnderecoUm", intent.getStringExtra("IEnderecoUm"))
                intentX.putExtra("IEnderecoDois", intent.getStringExtra("IEnderecoDois"))
                intentX.putExtra("IEnderecoTres", intent.getStringExtra("IEnderecoTres"))
                intentX.putExtra("ICurriculo", intent.getStringExtra("ICurriculo"))
                startActivity(intentX)
            }, 3000)
            //intentX.putExtra("fotoPerfil", foto)
            //startActivity(intentX)
        }
    }

    private fun startCamera(){
        cameraProviderFuture.addListener({

            imageCapture = ImageCapture.Builder().build()

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            }catch (e: Exception){
                Log.e("CameraPreview", "Falha ao abrir a camera.")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto():String{
        lateinit var file: File
        imageCapture?.let{
            val fileName = "JPEG_${System.currentTimeMillis()}.jpeg"
            file = File(externalMediaDirs[0], fileName)
            //intentX.putExtra("fotoPerfil", file.toUri().toString())
            //lateinit var path: String
            //path = Environment.getExternalStorageDirectory().toString() + fileName
            //intentX.putExtra("fotoPerfil", BitmapFactory.decodeFile(path))

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i("CameraPreview", "A imagem foi salva no diretório: ${file.toUri()}")
                        lateinit var path: String
                        path = Environment.getExternalStorageDirectory().toString() + fileName
                        intentX.putExtra("fotoPerfil", BitmapFactory.decodeFile(file.absolutePath))
                        intentX.putExtra("Result", "OK")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(binding.root.context, "Erro ao salvar foto.", Toast.LENGTH_SHORT).show()
                        Log.e("CameraPreview", "Exceção ao gravar arquivo da foto: $exception")
                    }
                })
            //lateinit var path: String
            //path = Environment.getExternalStorageDirectory().toString() + fileName
            //intentX.putExtra("fotoPerfil", BitmapFactory.decodeFile(file.absolutePath))
            return file.absolutePath
        }
        return file.absolutePath
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun blinkPreview(){
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
    }
}