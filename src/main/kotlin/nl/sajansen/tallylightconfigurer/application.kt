package nl.sajansen.tallylightconfigurer

import com.fazecast.jSerialComm.SerialPort
import nl.sajansen.tallylightconfigurer.gui.MainFrame
import nl.sajansen.tallylightconfigurer.serial.ESP32TallyLightDevice
import java.awt.EventQueue
import java.util.logging.Logger
import kotlin.system.exitProcess


private val logger = Logger.getLogger("Application")

fun main(args: Array<String>) {
    logger.info("Starting application ${ApplicationInfo.artifactId}:${ApplicationInfo.version}")
    logger.info("Executing JAR directory: " + getCurrentJarDirectory(ApplicationInfo).absolutePath)

    listSerialPorts()

    EventQueue.invokeLater {
        MainFrame.createAndShow()
    }
}

fun listSerialPorts() {
    println("Available serial ports: ")
    logger.info("Available serial ports: ")
    SerialPort.getCommPorts().forEach {
        println("- ${it.descriptivePortName} \t[${it.systemPortName}]")
        logger.info("- ${it.descriptivePortName} \t[${it.systemPortName}]")
    }
}

fun exitApplication() {
    logger.info("Shutting down application")

    ESP32TallyLightDevice.disconnect()

    logger.info("Shutdown finished")
    exitProcess(0)
}