package com.example.domweather.widget

import android.content.Context
import androidx.compose.foundation.layout.absolutePadding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.glance.Button
import androidx.glance.layout.Column
import androidx.glance.text.Text
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import com.example.domweather.repository.OpenWeatherRepositoryImpl
import com.example.domweather.valuesListCZ
import com.example.domweather.valuesListSK
import java.util.Calendar


class TemperatureWidget : GlanceAppWidget() {
    // Error ui ^ (Widgets dont crash)

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {

            fun celsiusToFahrenheit(celsius: Double): Double {
                return celsius * 9 / 5 + 32
            }

            val repository = remember { OpenWeatherRepositoryImpl(context) }

            val temp by repository.getStoredTemp("Indianapolis").collectAsState(initial = 0)
            
            val dayOfYearOrder = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

            val nextValueSK = valuesListSK[dayOfYearOrder]
            val nextValueCZ = valuesListCZ[dayOfYearOrder]


            WidgetContent(temp, nextValueSK, nextValueCZ)
        }
    }
}

@Composable
fun WidgetContent(temp: Int, namesSK: String, namesCZ: String) {
Row {
    Column(
        modifier = GlanceModifier.padding(2.dp)
    ) {
        Column {
            Text(
                text = "StA: 8°C/53°F"
            )
            Text(
                text = "HS: "
            )
            Text(
                text = "FM: "
            )
        }
    }
    Column(
    ) {
        Column {
            /*Text(
                text = "$temp°F",
                modifier = GlanceModifier
                    .padding(16.dp),
            )
            Text(
                text = "Indianapolis, IN",
                modifier = GlanceModifier
                    .padding(
                        horizontal = 16.dp
                    ),
            )*/
            Text(
                text = "SK: $namesSK",
                modifier = GlanceModifier
                    .padding(2.dp),
            )
            Text(
                text = "CZ: $namesCZ",
                modifier = GlanceModifier
                    .padding(2.dp),
            )
            /*Button(
                text = "Sync",
                modifier = GlanceModifier
                    .padding(16.dp),
                onClick = actionRunCallback<SyncAction>()
            )*/
        }
    }
}
}

class SyncAction : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val repository = OpenWeatherRepositoryImpl(context)

        repository.updateStoredTemp("Indianapolis, IN")
    }
}
