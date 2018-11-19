package dk.pme.kim.storageexample

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

	private val url = "gs://storageexample-916c1.appspot.com"

	//	Values for uploading:
	private val mobilePath = "/storage/emulated/0/Download/onlinedata.txt"
	private val firebasePath = "Test/onlinedata.txt"

	//	Values for downloading:
	private val filename = "GitHub App.pdf"
	private val prefix = "test_prefix"
	private val suffix = ".pdf"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		//	Remember to enable usage of external data for app:
		val file = Uri.fromFile(File(mobilePath))

		uploadFile(url, file, firebasePath)
		downloadFile(url, filename, prefix, suffix)
	}

	//	Upload file:
	fun uploadFile(url : String, file : Uri, path : String){
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
	fun downloadFile(url : String, filename : String, prefix : String, suffix : String){
		val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
		val fileRef = storageRef.child(filename)
		val localFile = File.createTempFile(prefix, "suffix")

		pBar_download.visibility = View.VISIBLE
		pBar_upload.progress = pBar_upload.min

		fileRef.getFile(localFile)
				.addOnSuccessListener {
					pBar_download.progress = pBar_upload.max
					//pBar_download.visibility = View.GONE
					Toast.makeText(this, "Download succesful!", Toast.LENGTH_LONG).show()
					Log.i("Path", fileRef.path)
					Log.i("AbsolutePath", localFile.absolutePath)
				}
				.addOnFailureListener {
					Toast.makeText(this, "Download unsuccesful!", Toast.LENGTH_LONG).show()
				}
	}
}
