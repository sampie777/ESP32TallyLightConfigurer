package nl.sajansen.tallylightconfigurer.gui

import nl.sajansen.tallylightconfigurer.exitApplication
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent


class MainFrameWindowAdapter(private val frame: MainFrame) : WindowAdapter() {
    override fun windowClosing(winEvt: WindowEvent) {
        exitApplication()
    }
}