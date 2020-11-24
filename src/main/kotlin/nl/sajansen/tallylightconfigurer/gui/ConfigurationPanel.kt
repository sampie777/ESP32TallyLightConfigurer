package nl.sajansen.tallylightconfigurer.gui

import nl.sajansen.tallylightconfigurer.serial.ESP32TallyLightDevice
import nl.sajansen.tallylightconfigurer.serial.SerialDevice
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

        ESP32TallyLightDevice.getWifiSsid { ssid ->
            ssidTextField.text = ssid
            setStatus("Loading password...")

            ESP32TallyLightDevice.getWifiPassword { password ->
                passwordTextField.text = password
                setStatus("Done")
            }
        }
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