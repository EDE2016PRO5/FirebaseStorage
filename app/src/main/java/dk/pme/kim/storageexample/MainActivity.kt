package dk.pme.kim.storageexample

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val url = "gs://storageexample-916c1.appspot.com"
		val mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)

		//	Upload file:
		val file = Uri.fromFile(File("/storage/emulated/0/Download/onlinedata.txt"))
		var fileRef = mStorageRef.child("SensorData/onlinedata.txt")

		//	Remember to enable usage of external data for app:
		fileRef.putFile(file)
				.addOnSuccessListener {
					Toast.makeText(this, "Upload succesful!", Toast.LENGTH_LONG).show()
				}
				.addOnFailureListener {
					Log.e("Upload_error_message", it.message)
					Log.e("Upload_error_stacktrace", it.stackTrace.toString())
					Log.e("Upload_error_cause", it.cause.toString())
					Toast.makeText(this, "Upload unsuccesful!", Toast.LENGTH_LONG).show()
				}

		//	Download file:
		val localFile = File.createTempFile("pdf", ".pdf")
		fileRef.getFile(localFile)
				.addOnSuccessListener {
					Toast.makeText(this, "Download succesful!", Toast.LENGTH_LONG).show()
				}
				.addOnFailureListener {
					Toast.makeText(this, "Download unsuccesful!", Toast.LENGTH_LONG).show()
				}
	}
}
