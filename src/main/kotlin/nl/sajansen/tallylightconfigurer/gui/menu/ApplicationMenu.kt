package nl.sajansen.tallylightconfigurer.gui.menu

import nl.sajansen.tallylightconfigurer.gui.utils.getMainFrameComponent
import nl.sajansen.tallylightconfigurer.exitApplication
import nl.sajansen.tallylightconfigurer.gui.MainFrame
import nl.sajansen.tallylightconfigurer.serial.ESP32TallyLightDevice
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.logging.Logger
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class ApplicationMenu : JMenu("Application") {
    private val logger = Logger.getLogger(ApplicationMenu::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        mnemonic = KeyEvent.VK_A

        val disconnectItem = JMenuItem("Disconnect")
        val infoItem = JMenuItem("Info")
        val quitItem = JMenuItem("Quit")

        // Set alt keys
        disconnectItem.mnemonic = KeyEvent.VK_D
        disconnectItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK or InputEvent.ALT_MASK)
        infoItem.mnemonic = KeyEvent.VK_I
        infoItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK or InputEvent.ALT_MASK)
        quitItem.mnemonic = KeyEvent.VK_Q
        quitItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK)

        disconnectItem.addActionListener {
            ESP32TallyLightDevice.disconnect()
            MainFrame.getInstance()?.showConnectionScreen()
        }
        infoItem.addActionListener { InfoFrame.createAndShow(getMainFrameComponent(this)) }
        quitItem.addActionListener { exitApplication() }

        add(disconnectItem)
        addSeparator()
        add(infoItem)
        add(quitItem)
    }
}