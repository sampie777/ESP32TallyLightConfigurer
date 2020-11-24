package nl.sajansen.tallylightconfigurer.gui

import com.fazecast.jSerialComm.SerialPort
import nl.sajansen.tallylightconfigurer.serial.ESP32TallyLightDevice
import java.awt.BorderLayout
import java.awt.GridLayout
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder


class ConnectionPanel : JPanel() {
    private val logger = Logger.getLogger(ConnectionPanel::class.java.name)

    private val serialPortList = JList<SerialPort>()
    private val baudRateSpinner = JSpinner()
    private val statusLabel = JLabel()

    init {
        createGui()
        refresh()
    }

    private fun createGui() {
        border = EmptyBorder(10, 10, 10, 10)
        layout = BorderLayout(10, 10)

        serialPortList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        baudRateSpinner.model = SpinnerNumberModel(115200, 1, Int.MAX_VALUE, 50)

        val refreshButton = JButton("Refresh").also {
            it.addActionListener { refresh() }
        }

        val connectButton = JButton("Connect").also {
            it.addActionListener { connect() }
        }

        val centerPanel = JPanel()
        centerPanel.layout = GridLayout(0, 1, 0, 5)
        centerPanel.add(JLabel("Device (COM port)"))
        centerPanel.add(serialPortList)
        centerPanel.add(JLabel("Baud rate"))
        centerPanel.add(baudRateSpinner)
        centerPanel.add(statusLabel)
        centerPanel.add(connectButton)
        centerPanel.add(refreshButton)

        add(centerPanel, BorderLayout.CENTER)
    }

    private fun refresh() {
        serialPortList.setListData(SerialPort.getCommPorts())
    }

    private fun connect() {
        val selectedPort: SerialPort? = serialPortList.selectedValue
        if (selectedPort == null) {
            return setStatus("Please select a device")
        }

        val baudRate = baudRateSpinner.value as Int
        if (baudRate <= 0) {
            return setStatus("Baud rate must be greater than 0")
        }

        if (!ESP32TallyLightDevice.connect(selectedPort, baudRate)){
            return setStatus("Failed to connect")
        }

        setStatus("Connected")

        MainFrame.getInstance()?.showConfigurationScreen()
    }

    private fun setStatus(status: String) {
        statusLabel.text = status
    }
}