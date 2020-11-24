package nl.sajansen.tallylightconfigurer.gui.menu

import nl.sajansen.tallylightconfigurer.gui.utils.ClickableLinkComponent
import nl.sajansen.tallylightconfigurer.gui.utils.DefaultDialogKeyDispatcher
import nl.sajansen.tallylightconfigurer.ApplicationInfo
import java.awt.Dimension
import java.awt.Font
import java.awt.KeyboardFocusManager
import javax.swing.*
import javax.swing.border.EmptyBorder

class InfoFrame(private val parentFrame: JFrame?) : JDialog(parentFrame) {

    companion object {
        fun create(parentFrame: JFrame?): InfoFrame = InfoFrame(parentFrame)

        fun createAndShow(parentFrame: JFrame?): InfoFrame {
            val frame = create(parentFrame)
            frame.isVisible = true
            return frame
        }
    }

    init {
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(DefaultDialogKeyDispatcher(this))

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.PAGE_AXIS)
        mainPanel.border = EmptyBorder(0, 20, 10, 20)
        add(mainPanel)

        val versionLabel = JLabel(
            "<html>" +
                    "<h1>${ApplicationInfo.name}</h1>" +
                    "<p>By ${ApplicationInfo.author}</p>" +
                    "<p>Version: ${ApplicationInfo.version}</p>" +
                    "</html>"
        )
        versionLabel.font = Font("Dialog", Font.PLAIN, 14)

        val sourceCodeLabel = ClickableLinkComponent(
            "${ApplicationInfo.name} online", ApplicationInfo.url
        )
        sourceCodeLabel.font = Font("Dialog", Font.PLAIN, 14)

        mainPanel.add(versionLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 10)))
        mainPanel.add(sourceCodeLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 20)))

        title = "Information"
        pack()
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
    }
}