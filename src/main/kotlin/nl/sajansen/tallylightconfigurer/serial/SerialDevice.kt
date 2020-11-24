package nl.sajansen.tallylightconfigurer.serial

import com.fazecast.jSerialComm.SerialPort
import java.util.logging.Logger

abstract class SerialDevice {
    private val logger = Logger.getLogger(SerialDevice::class.java.name)

    @Volatile
    private var comPort: SerialPort? = null
    fun getComPort() = comPort

    fun isConnected() = comPort != null

    abstract val serialListener: SerialListener

    open fun connect(device: SerialPort, baudRate: Int): Boolean {
        return connect(device.systemPortName, baudRate)
    }

    open fun connect(deviceName: String, baudRate: Int): Boolean {
        logger.info("Connecting to serial device '$deviceName' with baud rate $baudRate")

        comPort = SerialPort.getCommPorts().find { it.systemPortName == deviceName }
        if (comPort == null) {
            logger.severe("Serial device '$deviceName' not found")
            return false
        }

        comPort!!.baudRate = baudRate

        val connected = comPort!!.openPort()

        if (!connected) {
            logger.severe("Could not connect to hardware device '$deviceName'")
            return false
        }

        logger.info("Connected to hardware device '$deviceName'")
        clearComPort()
        comPort!!.addDataListener(serialListener)

        return true
    }

    open fun disconnect() {
        logger.info("Disconnecting hardware")
        comPort?.closePort()
        logger.info("Hardware device disconnected")
    }

    fun clearComPort() {
        logger.fine("Clearing com port buffer...")
        while (comPort!!.bytesAvailable() > 0) {
            val byteBuffer = ByteArray(comPort!!.bytesAvailable())
            comPort?.readBytes(byteBuffer, byteBuffer.size.toLong())
        }
        logger.fine("Com port buffer cleared")
    }

    abstract fun processSerialInput(messages: List<String>)

    open fun send(data: String) {
        serialListener.send(data)
    }
}