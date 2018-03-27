package kr.ifttutilities.bleMiClient

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_mi_client.*
import kr.ifttutilities.R
import kr.ifttutilities.toast


class MiClientActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var adapter: BluetoothAdapter
    private var scanHandler: Handler? = null
    private val REQUEST_ENABLE_BT = 100
    private val REQUEST_ENABLE_FINE_LOCATION = 101
    private val REQUEST_ENABLE_READ_SMS = 102
    private val SCAN_PERIOD_MILLIS: Long = 10000
    private var scanning = false
    private val TAG = "MiClientActivity"
    private var miService: MiClientUtilitiesService? = null
    private var isMiServiceConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_client)
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            toast("Feature not supported")
            finish()
        }

        //todo write a message about bonded device condition

        connectButton.setOnClickListener {
            val bluetoothManager: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            adapter = bluetoothManager.adapter
            if (checkForBondedMiBandDevice()) {
                Log.d(TAG, "device found starting service")
                startUtilityService()
            } else {
                //todo write a message about bonded device condition
            }
        }

        buttonTest.setOnClickListener {
            miService?.sendMessageToMiBand("testing")
        }
        buttonDisconnect.setOnClickListener {
            miService?.disconnect()
        }
    }

    private fun checkForBondedMiBandDevice(): Boolean {
        return adapter.bondedDevices?.any { it.name.startsWith("Mi Band") } ?: false
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        isMiServiceConnected = false
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        isMiServiceConnected = true
        val binder = service as MiClientUtilitiesService.MiBinder
        miService = binder.getService()
    }

    private val scanCallback = BluetoothAdapter.LeScanCallback { device, p1, p2 ->
        Log.d(TAG, device.address)
        if (device.address == BAND_REMOTE_ADDRESS) {
            // todo save in preference or something
            stopScan()
            Log.d(TAG, "device found starting service")
            startUtilityService()
        }
    }

    private fun startScan() {
        if (!scanning && hasPermissions()) {
            Log.d(TAG, "starting scan all permissions acquired")
            val filter = adapter?.bondedDevices?.filter { it.name.startsWith("Mi Band") }
            if (filter == null || filter.isEmpty()) {
                adapter?.startLeScan(scanCallback)
                scanHandler = Handler().also { it.postDelayed({ stopScan() }, SCAN_PERIOD_MILLIS) }
            } else {
                Log.d(TAG, "device already bonded")
                startUtilityService()
            }
        }
    }

    private fun startUtilityService() {
        val serviceIntent = Intent(this, MiClientUtilitiesService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }

    private fun stopScan() {
        adapter?.stopLeScan(scanCallback)
        Log.d(TAG, "stopping scan")
    }


    private fun hasPermissions(): Boolean {
        if (!adapter!!.isEnabled) {
            return false
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ENABLE_FINE_LOCATION)
            return false
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), REQUEST_ENABLE_READ_SMS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan()
        } else if (requestCode == REQUEST_ENABLE_READ_SMS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            startScan()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopScan()
        if (isMiServiceConnected)
            unbindService(this)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MiClientActivity::class.java)
        }
    }

}
