package nl.sajansen.tallylightconfigurer.gui

import nl.sajansen.tallylightconfigurer.serial.ESP32TallyLightDevice
import java.awt.BorderLayout
import java.awt.GridLayout
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.EmptyBorder


class ConfigurationPanel : JPanel() {
    private val logger = Logger.getLogger(ConfigurationPanel::class.java.name)

    private val ssidTextField = JTextField()
    private val passwordTextField = JTextField()
    private val statusLabel = JLabel()

    init {
        createGui()

        download()
    }

    private fun createGui() {
        border = EmptyBorder(10, 10, 10, 10)
        layout = BorderLayout(10, 10)

        val centerPanel = JPanel()
        centerPanel.layout = GridLayout(0, 1, 5, 5)
        centerPanel.add(JLabel("SSID"))
        centerPanel.add(ssidTextField)
        centerPanel.add(JLabel("Password"))
        centerPanel.add(passwordTextField)
        centerPanel.add(statusLabel)

        val uploadButton = JButton("Store").also {
            it.addActionListener { upload() }
        }
        val downloadButton = JButton("Load").also {
            it.addActionListener { download() }
        }

        val bottomPanel = JPanel()
        bottomPanel.layout = GridLayout(1, 0, 5, 5)
        bottomPanel.add(downloadButton)
        bottomPanel.add(uploadButton)

        add(centerPanel, BorderLayout.CENTER)
        add(bottomPanel, BorderLayout.PAGE_END)
    }

    private fun download() {
        setStatus("Loading SSID...")

        ESP32TallyLightDevice.getWifiSsid(::handleGetWifiSsidResponse)
    }

    private fun handleGetWifiSsidResponse(ssid: String): Boolean {
        if (ssid == "Booting..." || ssid == "Ready.") {
            logger.info("Can't get SSID: Device still booting. Retrying...")
            Thread.sleep(1500)
            download()
            return true
        }

        if (!ssid.startsWith("${ESP32TallyLightDevice.WIFI_SSID_GET}:")) {
            logger.info("Wrong SSID response: $ssid")
            return false
        }

        ssidTextField.text = ssid.substringAfter("${ESP32TallyLightDevice.WIFI_SSID_GET}:")
        setStatus("Loading password...")

        ESP32TallyLightDevice.getWifiPassword { password ->
            if (!password.startsWith("${ESP32TallyLightDevice.WIFI_PASSWORD_GET}:")) {
                logger.info("Wrong password response: $password")
                return@getWifiPassword false
            }

            passwordTextField.text = password.substringAfter("${ESP32TallyLightDevice.WIFI_PASSWORD_GET}:")
            setStatus("Done")
            return@getWifiPassword true
        }
        return true
    }

    private fun upload() {
        setStatus("Storing...")
        val ssid = ssidTextField.text
        val password = passwordTextField.text

        ESP32TallyLightDevice.setWifiSsid(ssid)
        Thread.sleep(100)
        ESP32TallyLightDevice.setWifiPassword(password)

        setStatus("Done")
    }

    private fun setStatus(status: String) {
        statusLabel.text = status
    }
}