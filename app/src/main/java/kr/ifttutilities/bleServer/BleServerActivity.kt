package kr.ifttutilities.bleServer

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_ble_server.*
import kr.ifttutilities.R
import kr.ifttutilities.toast

//todo problem with location permission

class BleServerActivity : AppCompatActivity() {

    private var scanning: Boolean = false
    private val REQUEST_ENABLE_BT = 100
    private val REQUEST_ENABLE_FINE_LOCATION = 101
    private val SCAN_PERIOD_MILLIS: Long = 5000

    private lateinit var adapter: BluetoothAdapter
    private var scanResults: Map<String, BluetoothDevice>? = null
    private var scanHandler: Handler? = null
    private val TAG = "BLE_SERVER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_server)
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // todo not supported feature
            toast("Feature not supported")
            finish()
        }
        val bluetoothManager: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        adapter = bluetoothManager.adapter

        buttonStartScan.setOnClickListener {
            startScan()
        }

    }

    private val scanCallback = BluetoothAdapter.LeScanCallback { device, p1, p2 ->
        addDevice(device, scanResults as MutableMap<String, BluetoothDevice>)
    }


    private fun startScan() {
        if (!scanning && hasPermissions()) {
            scanResults = mutableMapOf()
            adapter.startLeScan(scanCallback)
            scanHandler = Handler().also { it.postDelayed({ stopScan() }, SCAN_PERIOD_MILLIS) }
        }
    }

    private fun stopScan() {
        adapter.stopLeScan(scanCallback)
    }

    private fun addDevice(device: BluetoothDevice, scanResults: MutableMap<String, BluetoothDevice>) {
        scanResults[device.address] = device
        Log.d(TAG, device.address)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "fine location request")
            startScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "bluetooth request")
            startScan()
        }
    }

    private fun hasPermissions(): Boolean {
        if (!adapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            return false
        }
        if (checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ENABLE_FINE_LOCATION)
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        stopScan()
        scanHandler?.removeCallbacksAndMessages(null)
    }


    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BleServerActivity::class.java)
        }
    }
}
