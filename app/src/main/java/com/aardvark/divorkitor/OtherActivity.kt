package com.aardvark.divorkitor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.aardvark.divorkitor.ui.theme.DivorKitorTheme
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration

class OtherActivity : ComponentActivity() {

    private val assetReader = AssetReader(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val divJson = assetReader.read("test.json")
        val templateJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(baseContext = this, configuration = createDivConfiguration())
        setContent {
            DivorKitorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AndroidView(
                        factory = {
                            DivViewFactory(divContext, templateJson).createView(cardJson)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        update = {
                            Log.d("DivorKitorTheme", "onCreate: DivView Inflated.")
                        }
                    )
                }
            }
        }
    }

    private fun createDivConfiguration(): DivConfiguration {
        return DivConfiguration.Builder(DemoDivImageLoader(this))
            .actionHandler(MainActivity.DemoDivActionHandler())
            .supportHyphenation(true)
            .visualErrorsEnabled(true)
            .build()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DivorKitorTheme {
        Greeting("Android")
    }
}