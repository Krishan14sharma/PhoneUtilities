package kr.ifttutilities.bleMiClient

import android.bluetooth.*
import android.content.Context
import android.util.Log
import kr.ifttutilities.AppPreferenceManager

/**
 * Created by krishan on 24/03/18.
 */
class MiBandHelper(val context: Context) : BluetoothGattCallback() {

    private var adapter: BluetoothAdapter
    private val TAG = "MiBandHelper"
    private var gatt: BluetoothGatt? = null

    init {
        val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        adapter = bluetoothManager.adapter
    }

    fun connectBand() {
        adapter.getRemoteDevice(AppPreferenceManager.getInstance(context).getMiBandAddress())
                .connectGatt(context, true, this)
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        this.gatt = gatt
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            Log.d(TAG, "connected with bt server")
            gatt.discoverServices()
        }
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
        super.onCharacteristicRead(gatt, characteristic, status)
    }

    override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
        super.onCharacteristicWrite(gatt, characteristic, status)
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        Log.d(TAG, "services discovered")
    }

    fun sendMessageNotification(message: String) {
        val characteristic = gatt?.getService(alertMessageServiceUUID)?.getCharacteristic(newAlertCharacteristicUUID)
        characteristic?.value = byteArrayOf(*sendMessageAlertProtocolHeader).plus(message.toByteArray())
        val b = gatt?.writeCharacteristic(characteristic)
        Log.d(TAG, b.toString())
    }

    fun disconnect() {
        gatt?.close()
    }
}