package kr.ifttutilities.bleMiClient

import java.util.*

/**
 * Created by krishan on 27/03/18.
 */

val alertMessageServiceUUID = UUID.fromString("00001811-0000-1000-8000-00805f9b34fb")
val newAlertCharacteristicUUID = UUID.fromString("00002a46-0000-1000-8000-00805f9b34fb")
val sendMessageAlertProtodcolHeader = byteArrayOf(0x05, 0x01) // 1. type of message 2. unread counts
val BAND_NAME = "Mi Band"