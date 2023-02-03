package com.xlebnick.kitties.utils

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import com.xlebnick.kitties.R
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileUtils @Inject constructor(private val context: Context) {

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private var outputDirectory: File = getOutputDirectory()

    @Suppress("DEPRECATION")
    fun getOutputDirectory(): File {
        val mediaDir = context.externalMediaDirs?.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            return mediaDir
        else
            context.filesDir
    }

    fun createImageCaptureOptions(): Pair<ImageCapture.OutputFileOptions, Uri> {
        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        return (ImageCapture.OutputFileOptions.Builder(photoFile)
            .build() to Uri.fromFile(photoFile))
    }


    fun convertImageUriToMultipart(fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)

        // create RequestBody instance from file
        val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaType())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }
}
