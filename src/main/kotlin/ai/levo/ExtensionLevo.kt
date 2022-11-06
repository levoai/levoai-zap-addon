package ai.levo

import org.apache.logging.log4j.kotlin.Logging
import org.parosproxy.paros.Constant
import org.parosproxy.paros.extension.ExtensionAdaptor
import org.parosproxy.paros.extension.ExtensionHook

class ExtensionLevo : ExtensionAdaptor(), Logging {

    override fun getName(): String = "levoai"
    override fun getI18nPrefix(): String = LevoConstants.I18N_PREFIX
    override fun getUIName(): String = Constant.messages.getString(LevoConstants.I18N_PREFIX + ".uiName")
    override fun getDescription(): String = Constant.messages.getString(LevoConstants.I18N_PREFIX + ".description")
    override fun getAuthor(): String = "Levo.ai"
    override fun canUnload(): Boolean = true

    override fun hook(extensionHook: ExtensionHook?) {
        super.hook(extensionHook)
    }

}