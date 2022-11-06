package ai.levo

import org.apache.logging.log4j.kotlin.Logging
import org.parosproxy.paros.Constant
import org.parosproxy.paros.extension.ExtensionAdaptor
import org.parosproxy.paros.extension.ExtensionHook

private const val I18N_PREFIX = "levoai"

class ExtensionLevo : ExtensionAdaptor(), Logging {

    internal val param by lazy { LevoParam() }

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
    }

}