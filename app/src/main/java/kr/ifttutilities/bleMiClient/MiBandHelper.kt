package kr.ifttutilities.bleMiClient

import android.bluetooth.*
import android.content.Context
import android.util.Log
import java.util.*

/**
 * Created by krishan on 24/03/18.
 */
class MiBandHelper(val context: Context) : BluetoothGattCallback() {

    private var adapter: BluetoothAdapter
    private val TAG = "MiBandHelper"
    private var gatt: BluetoothGatt? = null

    val alertMessageUUID = UUID.fromString("00001811-0000-1000-8000-00805f9b34fb")
    val newAlertCharacteristicUUID = UUID.fromString("00002a46-0000-1000-8000-00805f9b34fb")
    val sendMessageAlertProtocol = byteArrayOf(0x05, 0x01)

    init {
        val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        adapter = bluetoothManager.adapter
    }

    fun connectBand() {
        adapter.getRemoteDevice("F3:BE:78:E5:71:71")
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
        val characteristic = gatt?.getService(alertMessageUUID)?.getCharacteristic(newAlertCharacteristicUUID)
        characteristic?.value = byteArrayOf(*sendMessageAlertProtocol).plus(message.toByteArray())
        //        characteristic.value = byteArrayOf(0x05, 0x00, 0x48, 0x69)// to send hi as a message
        val b = gatt?.writeCharacteristic(characteristic)
        Log.d(TAG, b.toString())
    }

    //    00001811-0000-1000-8000-00805f9b34fb
    //    00002a46-0000-1000-8000-00805f9b34fb
    //    00002a44-0000-1000-8000-00805f9b34fb    ---->00002902-0000-1000-8000-00805f9b34fb
    fun disconnect() {

    }

}