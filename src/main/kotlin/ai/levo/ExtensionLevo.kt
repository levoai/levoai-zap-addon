/*
 * Copyright 2022 Levo.ai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            extensionHook?.hookView?.addOptionPanel(LevoOptionsPanel())
            extensionHook?.hookView?.addMainToolBarComponent(toolbarButton)
        }
    }

    override fun optionsLoaded() {
        toolbarButton.isSelected = param.enabled
    }
}
