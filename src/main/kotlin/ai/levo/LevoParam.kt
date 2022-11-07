package ai.levo

import org.apache.commons.configuration.ConversionException
import org.apache.logging.log4j.kotlin.Logging
import org.zaproxy.zap.common.VersionedAbstractParam

private const val PARAM_BASE_KEY = "levoai"
private const val PARAM_CURRENT_VERSION: Int = 1
private const val PARAM_ENABLED = "$PARAM_BASE_KEY.enabled"
private const val PARAM_SATELLITE_URL = "$PARAM_BASE_KEY.satelliteUrl"

class LevoParam : VersionedAbstractParam(), Logging {

    internal var enabled: Boolean = false
        set(value) {
            field = value
            config.setProperty(PARAM_ENABLED, value)
        }

    internal var satelliteUrl: String = "http://localhost:9999"
        set(value) {
            field = value
            config.setProperty(PARAM_SATELLITE_URL, value)
        }

    override fun getConfigVersionKey(): String = PARAM_BASE_KEY + VERSION_ATTRIBUTE
    override fun getCurrentVersion(): Int = PARAM_CURRENT_VERSION
    override fun updateConfigsImpl(fileVersion: Int) {}

    override fun parseImpl() {
        try {
            enabled = config.getBoolean(PARAM_ENABLED, enabled)
            satelliteUrl = config.getString(PARAM_SATELLITE_URL, satelliteUrl)
        } catch (e: ConversionException) {
            logger.error("Error parsing levoai configuration: ${e.message}", e)
        }
    }
}