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

import org.apache.commons.httpclient.URI
import org.parosproxy.paros.Constant
import org.parosproxy.paros.model.OptionsParam
import org.parosproxy.paros.view.AbstractParamPanel
import org.zaproxy.zap.utils.ZapTextField
import org.zaproxy.zap.view.LayoutHelper
import java.awt.GridBagLayout
import java.awt.Insets
import java.net.URL
import javax.swing.JLabel

private val NAME = Constant.messages.getString("levoai.options.panelName")

class LevoOptionsPanel : AbstractParamPanel() {

    private val satelliteUrl: ZapTextField by lazy { ZapTextField() }

    init {
        name = NAME
        layout = GridBagLayout()

        var rowIndex: Int = -1

        val satelliteUrlLabel = JLabel(Constant.messages.getString("levoai.options.satelliteUrl"))
        add(satelliteUrlLabel, LayoutHelper.getGBC(0, ++rowIndex, 1, 0.2, Insets(2, 2, 2, 2)))
        add(satelliteUrl, LayoutHelper.getGBC(1, rowIndex, 1, 0.8, Insets(2, 2, 2, 2)))
        add(JLabel(), LayoutHelper.getGBC(0, ++rowIndex, 2, 1.0, 1.0))
    }

    override fun initParam(obj: Any?) {
        val levoParam = (obj as OptionsParam).getParamSet(LevoParam::class.java)
        satelliteUrl.text = levoParam.satelliteUrl
    }

    override fun validateParam(obj: Any?) {
        try {
            URL(satelliteUrl.text).toURI()
            URI(satelliteUrl.text, true)
        } catch (e: Exception) {
            throw IllegalArgumentException(Constant.messages.getString("levoai.error.invalidUrl", e.localizedMessage))
        }
    }

    override fun saveParam(obj: Any?) {
        val levoParam = (obj as OptionsParam).getParamSet(LevoParam::class.java)
        levoParam.satelliteUrl = satelliteUrl.text
    }
}
