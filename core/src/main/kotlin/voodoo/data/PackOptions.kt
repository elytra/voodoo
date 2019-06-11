package voodoo.data

import com.skcraft.launcher.model.SKServer
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class PackOptions(
    var multimcOptions: MultiMC = MultiMC(),
    var skCraftOptions: SKCraft = SKCraft(),
    var baseUrl: String? = null
) {
    fun multimc(configure: MultiMC.() -> Unit) {
        multimcOptions.configure()
    }
    fun skcraft(configure: SKCraft.() -> Unit) {
        skCraftOptions.configure()
    }

    @Serializable data class MultiMC(
        var skPackUrl: String? = null
    )
    @Serializable data class SKCraft(
        var userFiles: UserFiles = UserFiles(),
        var server: SKServer? = null,
        var thumb: String? = null
    )
}
