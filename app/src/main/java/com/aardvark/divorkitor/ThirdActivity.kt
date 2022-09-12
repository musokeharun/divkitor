package com.aardvark.divorkitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aardvark.divorkitor.databinding.ActivityThirdBinding
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div2.DivData
import org.json.JSONObject

class ThirdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdBinding
    private val assetReader = AssetReader(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val divJson = assetReader.read("test.json")
        val templateJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(baseContext = this, configuration = createDivConfiguration())
        binding.container.addView(DivViewFactory(divContext, templateJson).createView(cardJson))
    }

    private fun createDivConfiguration(): DivConfiguration {
        return DivConfiguration.Builder(DemoDivImageLoader(this))
            .actionHandler(MainActivity.DemoDivActionHandler())
            .supportHyphenation(true)
            .visualErrorsEnabled(true)
            .build()
    }
}


class DivViewFactory(
    private val context: Div2Context,
    private val templatesJson: JSONObject? = null
) {

    private val environment = DivParsingEnvironment(ParsingErrorLogger.ASSERT).apply {
        if (templatesJson != null) parseTemplates(templatesJson)
    }

    fun createView(cardJson: JSONObject): Div2View {
        val divData = DivData(environment, cardJson)
        return Div2View(context).apply {
            setData(divData, DivDataTag(divData.logId))
        }
    }
}