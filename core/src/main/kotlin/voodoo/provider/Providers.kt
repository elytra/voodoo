package voodoo.provider

import mu.KLogging
import voodoo.data.flat.FlatEntry
import voodoo.data.nested.NestedEntry

object Providers : KLogging() {
    private val providers = hashMapOf<String, ProviderBase>()

    init {
        register(
            "CURSE" to CurseProvider,
            "JENKINS" to JenkinsProvider,
            "DIRECT" to DirectProvider,
            "LOCAL" to LocalProvider,
            "NOOP" to NoopProvider
        )
    }

    fun forEntry(entry: FlatEntry) =
        when(entry) {
            is FlatEntry.Common -> null
            is FlatEntry.Curse -> CurseProvider
            is FlatEntry.Jenkins -> JenkinsProvider
            is FlatEntry.Direct -> DirectProvider
            is FlatEntry.Local -> LocalProvider
            is FlatEntry.Noop -> NoopProvider
        }

    fun forEntry(entry: NestedEntry) =
        when(entry) {
            is NestedEntry.Common -> null
            is NestedEntry.Curse -> CurseProvider
            is NestedEntry.Jenkins -> JenkinsProvider
            is NestedEntry.Direct -> DirectProvider
            is NestedEntry.Local -> LocalProvider
            is NestedEntry.Noop -> NoopProvider
        }

    fun register(vararg pairs: Pair<String, ProviderBase>) {
        pairs.forEach { (key, provider) ->
            providers[key.toUpperCase()]?.let { existing ->
                logger.warn("overriding existing provider ${existing.name}")
            }

            providers[key.toUpperCase()] = provider
        }
    }

    operator fun get(key: String) = providers[key.toUpperCase()]
        ?: throw IllegalArgumentException("cannot find provider for key '${key.toUpperCase()}'")

    fun getId(provider: ProviderBase): String? {
        for ((id, registeredProvider) in providers) {
            if (provider == registeredProvider) {
                return id
            }
        }
        logger.error("found no matching registered provider")
        return null
    }
}