import voodoo.dsl.builder.AbstractBuilder
import voodoo.dsl.builder.EntryBuilder
import voodoo.provider.DirectProvider

// DIRECT

var EntryBuilder<DirectProvider>.url
    get() = entry.url
    set(it) {
        entry.url = it
    }
var AbstractBuilder<DirectProvider>.useUrlTxt
    get() = entry.useUrlTxt
    set(it) {
        entry.useUrlTxt = it
    }

inline infix fun <reified T> T.url(s: String) where T : EntryBuilder<DirectProvider> =
    apply { entry.url = s }

inline infix fun <reified T> T.useUrlTxt(b: Boolean) where T : EntryBuilder<DirectProvider> =
    apply { entry.useUrlTxt = b }