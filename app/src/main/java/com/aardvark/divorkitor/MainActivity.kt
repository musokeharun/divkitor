package com.aardvark.divorkitor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aardvark.divorkitor.databinding.ActivityMainBinding
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.DivViewFacade
import com.yandex.div2.DivAction

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val assetReader = AssetReader(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val divJson = assetReader.read("feed.json")
        val templatesJson = divJson.optJSONObject("templates")
        val cardsJson = divJson.getJSONArray("cards")

        val divContext = Div2Context(baseContext = this, configuration = createDivConfiguration())
        val listAdapter = DivListAdapter(divContext, templatesJson, cardsJson)
        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        binding.goButton.setOnClickListener {
            startActivity(Intent(applicationContext, ThirdActivity::class.java))
        }

        binding.goButtonCompose.setOnClickListener {
            startActivity(Intent(applicationContext, OtherActivity::class.java))
        }

    }

    private fun createDivConfiguration(): DivConfiguration {
        return DivConfiguration.Builder(DemoDivImageLoader(this))
            .actionHandler(DemoDivActionHandler())
            .supportHyphenation(true)
            .visualErrorsEnabled(true)
            .build()
    }

    class DemoDivActionHandler : DivActionHandler() {
        override fun handleAction(action: DivAction, view: DivViewFacade): Boolean {
            val uri = action.url!!.evaluate(view.expressionResolver)
            Log.d(
                "DemoDivActionHandler",
                "handleAction: ${action.url} ${uri.authority} ${uri.scheme}"
            )


            return super.handleAction(action, view)
        }
    }

}