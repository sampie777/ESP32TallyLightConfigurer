package nl.sajansen.tallylightconfigurer.gui


import nl.sajansen.tallylightconfigurer.ApplicationInfo
import nl.sajansen.tallylightconfigurer.gui.menu.MenuBar
import java.awt.EventQueue
import java.util.logging.Logger
import javax.swing.JFrame
import javax.swing.JPanel

class MainFrame : JFrame() {
    private val logger = Logger.getLogger(MainFrame::class.java.name)

    companion object {
        private var instance: MainFrame? = null
        fun getInstance() = instance

        fun create(): MainFrame = MainFrame()

        fun createAndShow(): MainFrame {
            val frame = create()
            frame.isVisible = true
            return frame
        }
    }

    private var mainPanel: JPanel = ConnectionPanel()

    init {
        instance = this

        addWindowListener(MainFrameWindowAdapter(this))

        initGUI()
    }

    private fun initGUI() {
        add(ConnectionPanel())

        jMenuBar = MenuBar()
        pack()
        title = ApplicationInfo.name
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    fun rebuildGui() {
        logger.info("Rebuilding main GUI")
        EventQueue.invokeLater {
            contentPane.removeAll()
            add(mainPanel)
            jMenuBar = MenuBar()

            revalidate()
            repaint()
            logger.info("GUI rebuild done")
        }
    }

    fun showConnectionScreen() {
        logger.info("Showing Connection Screen")
        if (mainPanel::class.java == ConnectionPanel::class.java) {
            logger.info("Connection screen already visible")
            return
        }

        mainPanel = ConnectionPanel()
        rebuildGui()
    }

    fun showConfigurationScreen() {
        logger.info("Showing Configuration Screen")
        if (mainPanel::class.java == ConfigurationPanel::class.java) {
            logger.info("Configuration screen already visible")
            return
        }

        mainPanel = ConfigurationPanel()
        rebuildGui()
    }
}