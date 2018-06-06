package voodoo.data

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Created by nikky on 29/03/18.
 * @author Nikky
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class SKFeature(
        var name: String = "",
        @JsonInclude(JsonInclude.Include.ALWAYS)
        var selected: Boolean = false,
        var description: String = "",
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var recommendation: Recommendation? = null
)