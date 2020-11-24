package nl.sajansen.tallylightconfigurer.serial


import java.util.logging.Logger

object ESP32TallyLightDevice : SerialDevice() {
    private val logger = Logger.getLogger(ESP32TallyLightDevice::class.java.name)

    override val serialListener = SerialListener(this)

    const val BOOT_INTO_CONFIG = "cnf"
    const val WIFI_SSID_SET = "wss"
    const val WIFI_SSID_GET = "wsg"
    const val WIFI_PASSWORD_SET = "wps"
    const val WIFI_PASSWORD_GET = "wpg"

    private val callbacks = arrayListOf<(value: String) -> Boolean>()

    override fun processSerialInput(messages: List<String>) {
        messages.forEach { 
            if (it.startsWith("[")) return@forEach

            if (it == "Booting...") {
                Thread.sleep(30)
                send(BOOT_INTO_CONFIG)
            }

            if (callbacks.size == 0) return@forEach
            
            if (callbacks.first().invoke(it)) {
                callbacks.removeAt(0)
            }
        }
    }

    fun setWifiSsid(value: String) {
        send(WIFI_SSID_SET + value)
    }

    fun getWifiSsid(callback: (value: String) -> Boolean) {
        callbacks.add(callback)
        send(WIFI_SSID_GET)
    }

    fun setWifiPassword(value: String) {
        send(WIFI_PASSWORD_SET + value)
    }

    fun getWifiPassword(callback: (value: String) -> Boolean) {
        callbacks.add(callback)
        send(WIFI_PASSWORD_GET)
    }
}