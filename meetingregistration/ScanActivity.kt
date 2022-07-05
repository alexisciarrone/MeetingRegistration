package lu.uni.mics.meetingregistration

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*

private const val CAMERA_REQUEST_CODE = 101

class ScanActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        setupPermissions()

        val scannerView = findViewById<CodeScannerView>(R.id.scannerView)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                registerParticipant(it.text)
                //Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun registerParticipant(eventName: String) {
        val dialogBox = AlertDialog.Builder(this)

        if (eventName.contains("Event")) {
            dialogBox.setMessage("Do you want to register to the event ${eventName}?")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent(this, ParticipantActivity::class.java).apply {
                        putExtra("event name", eventName)
                    }
                    startActivity(intent)
                    finish()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->
                    codeScanner.startPreview()
                })

            val confirmRegistration = dialogBox.create()
            confirmRegistration.setTitle("Event or meeting found!")
            confirmRegistration.show()
        } else {
            dialogBox.setMessage("The QR code does not correspond to an event or meeting. Please scan a valid QR code.")
                .setCancelable(false)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                    codeScanner.startPreview()
                })
            val invalidQR = dialogBox.create()
            invalidQR.setTitle("Invalid QR code")
            invalidQR.show()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions () {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the camera permission!", Toast.LENGTH_SHORT).show()
                } else {
                    // successful
                }
            }
        }
    }
}