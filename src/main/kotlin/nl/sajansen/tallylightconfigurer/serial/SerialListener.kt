package nl.sajansen.tallylightconfigurer.serial


import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import java.util.logging.Logger

class SerialListener(private val serialDevice: SerialDevice) : SerialPortDataListener {
    private val logger = Logger.getLogger(SerialListener::class.java.name)

    private var keepAllMessages: Boolean = false
    var currentDataLine: String = ""
    val receivedDataLines = ArrayList<String>()

    override fun getListeningEvents(): Int {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED
    }

    override fun serialEvent(event: SerialPortEvent) {
        if (event.eventType != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
            logger.warning("Got invalid event type: ${event.eventType}")
            return
        }

        onDataReceived(event.receivedData)
    }

    private fun onDataReceived(data: ByteArray) {
        currentDataLine += String(data)
        val messages = currentDataLine
            .split("\n")
            .map { it.trim('\r') }

        val terminatedMessages = messages.subList(0, messages.size - 1)
        currentDataLine = messages.last()

        if (keepAllMessages) {
            receivedDataLines.addAll(terminatedMessages)
        }
        terminatedMessages.forEach {
            logger.info("Serial data: $it")
        }

        serialDevice.processSerialInput(terminatedMessages)
    }

    fun send(data: String) {
        logger.info("Sending data to serial device: $data")

        if (!serialDevice.isConnected()) {
            logger.warning("Serial device unconnected, cannot send data")
            return
        }

        val dataBytes = "${data}\n".toByteArray()
        val writtenBytes = serialDevice.getComPort()?.writeBytes(dataBytes, dataBytes.size.toLong())

        if (writtenBytes != dataBytes.size) {
            logger.warning("Not all bytes were sent. Only $writtenBytes out of ${dataBytes.size}")
        }
    }

    fun clear() {
        logger.fine("Clearing serial data buffer")
        receivedDataLines.clear()
        currentDataLine = ""
    }
}