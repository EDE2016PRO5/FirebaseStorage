package dk.pme.kim.storageexample

import android.app.Activity
import android.app.usage.ExternalStorageStats
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

	//	Array with permissions:
	private var permissionsRequired = arrayOf(
			android.Manifest.permission.INTERNET,
			android.Manifest.permission.READ_EXTERNAL_STORAGE,
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

	//	Values for uploading:
	private val mobilePath_upload = "/storage/emulated/0/Download/onlinedata.txt"
	private val firebasePath_upload = "Test/onlinedata.txt"

	//	Values for downloading:
	private val mobilePath_download = Environment.getExternalStorageDirectory().toString()+"/Download"
	private val firebasePath_download = "Test/onlinedata.txt"

	//	Firebase url and file to upload:
	private val url = "gs://storageexample-916c1.appspot.com"
	val file = Uri.fromFile(File(mobilePath_upload))

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		//	Request permission from user - since R/W is considered as dangerous operations, it is not enough to include them in the manifest file:
		ActivityCompat.requestPermissions(this, permissionsRequired, 123)
	}

	//	Upload file:
	fun uploadFile(url : String, path : String, file : Uri){
		val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
		val fileRef = storageRef.child(path)

		pBar_upload.visibility = View.VISIBLE
		pBar_upload.progress = pBar_upload.min

		fileRef.putFile(file)
				.addOnSuccessListener {
					pBar_upload.progress = pBar_upload.max
					//pBar_upload.visibility = View.GONE
					Toast.makeText(this, "Upload succesful!", Toast.LENGTH_LONG).show()
				}
				.addOnFailureListener {
					Log.e("Upload_error_message", it.message)
					Log.e("Upload_error_stacktrace", it.stackTrace.toString())
					Log.e("Upload_error_cause", it.cause.toString())
					Toast.makeText(this, "Upload unsuccesful!", Toast.LENGTH_LONG).show()
				}
	}

	//	Download file:
	fun downloadFile(url : String, path : String, prefix : String, suffix : String, outPath : String){
		val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
		val fileRef = storageRef.child(path)

		pBar_download.visibility = View.VISIBLE
		pBar_upload.progress = pBar_upload.min

		//	Using createTempFile will create a cache file.
		//val file = File.createTempFile(prefix, suffix, File(outPath))
		val file = File(outPath, prefix+suffix)

		fileRef.getFile(file)
				.addOnSuccessListener {
					pBar_download.progress = pBar_upload.max
					//pBar_download.visibility = View.GONE
					Toast.makeText(this, "Download succesful!", Toast.LENGTH_LONG).show()
				}
				.addOnFailureListener {
					Log.i("Path", fileRef.path)
					Log.i("AbsolutePath", file.path)
					Log.i("Download_error_cause", it.cause.toString())
					Toast.makeText(this, "Download unsuccesful!", Toast.LENGTH_LONG).show()
				}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)

		if(requestCode == 123){
			if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					grantResults[1] == PackageManager.PERMISSION_GRANTED &&
					grantResults[2] == PackageManager.PERMISSION_GRANTED){
                uploadFile(url, firebasePath_upload, file)
                downloadFile(url, firebasePath_download, "Kim", ".txt", mobilePath_download)
			}

			else{
				Toast.makeText(this, "Permissions need to be allowed...", Toast.LENGTH_SHORT).show()
			}
		}
	}

}