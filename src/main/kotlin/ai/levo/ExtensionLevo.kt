package ai.levo

import org.apache.logging.log4j.kotlin.Logging
import org.parosproxy.paros.Constant
import org.parosproxy.paros.extension.ExtensionAdaptor
import org.parosproxy.paros.extension.ExtensionHook
import org.zaproxy.zap.view.ZapToggleButton
import javax.swing.ImageIcon

private const val I18N_PREFIX = "levoai"
private val LEVO_ICON by lazy {
    ImageIcon(ExtensionLevo::class.java.getResource("/ai/levo/resources/levo-icon.png"))
}

class ExtensionLevo : ExtensionAdaptor(), Logging {

    internal val param: LevoParam by lazy { LevoParam() }

    private val toolbarButton: ZapToggleButton by lazy {
        ZapToggleButton(LEVO_ICON, param.enabled).apply {
            toolTipText = Constant.messages.getString("$I18N_PREFIX.toolbar.button.tooltip.enable")
            selectedToolTipText = Constant.messages.getString("$I18N_PREFIX.toolbar.button.tooltip.disable")
            addActionListener { param.enabled = this.isSelected }
        }
    }

    override fun getName(): String = "levoai"
    override fun getI18nPrefix(): String = I18N_PREFIX
    override fun getUIName(): String = Constant.messages.getString("$I18N_PREFIX.uiName")
    override fun getDescription(): String = Constant.messages.getString("$I18N_PREFIX.description")
    override fun getAuthor(): String = "Levo.ai"
    override fun canUnload(): Boolean = true

    override fun hook(extensionHook: ExtensionHook?) {
        super.hook(extensionHook)

        extensionHook?.addOptionsParamSet(param)
        extensionHook?.addHttpSenderListener(LevoHttpSenderListener(this))

        if (hasView()) {
            extensionHook?.hookView?.addMainToolBarComponent(toolbarButton)
        }
    }

    override fun optionsLoaded() {
        toolbarButton.isSelected = param.enabled
    }
}