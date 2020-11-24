package nl.sajansen.tallylightconfigurer.serial


import java.util.logging.Logger

object ESP32TallyLightDevice : SerialDevice() {
    private val logger = Logger.getLogger(ESP32TallyLightDevice::class.java.name)

    override val serialListener = SerialListener(this)
    
    private val callbacks = arrayListOf<(value: String) -> Unit>()

    override fun processSerialInput(messages: List<String>) {
        messages.forEach { 
            if (it.startsWith("[")) return@forEach
            
            if (callbacks.size == 0) return@forEach
            
            callbacks.first().invoke(it)
            callbacks.removeAt(0)
        }
    }

    fun setWifiSsid(value: String) {
        send("wss${value}")
    }

    fun getWifiSsid(callback: (value: String) -> Unit) {
        callbacks.add(callback)
        send("wsg")
    }

    fun setWifiPassword(value: String) {
        send("wps${value}")
    }

    fun getWifiPassword(callback: (value: String) -> Unit) {
        callbacks.add(callback)
        send("wpg")
    }
}