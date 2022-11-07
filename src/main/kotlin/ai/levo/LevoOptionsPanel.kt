package ai.levo

import org.parosproxy.paros.Constant
import org.parosproxy.paros.model.OptionsParam
import org.parosproxy.paros.view.AbstractParamPanel
import org.zaproxy.zap.utils.ZapTextField
import org.zaproxy.zap.view.LayoutHelper
import java.awt.GridBagLayout
import java.awt.Insets
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

    override fun saveParam(obj: Any?) {
        val levoParam = (obj as OptionsParam).getParamSet(LevoParam::class.java)
        levoParam.satelliteUrl = satelliteUrl.text
    }
}