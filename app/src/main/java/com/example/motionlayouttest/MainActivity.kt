package com.example.motionlayouttest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.example.motionlayouttest.ui.theme.MotionLayoutTestTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMotionApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotionLayoutTestTheme {
                val progress = remember { mutableStateOf(0f) }
                val isScrollEnable = remember { mutableStateOf(false)}
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.heightIn(20.dp))
                    Slider(
                        value = progress.value,
                        onValueChange = {
                            Log.d("testing", "$it")
                            progress.value = it
                            isScrollEnable.value = false
                        },
                        onValueChangeFinished = {
                            isScrollEnable.value = true
                        }
                    )
                    Spacer(modifier = Modifier.heightIn(20.dp))
                    val context = LocalContext.current
                    val scene = remember {
                        context.resources
                            .openRawResource(R.raw.motion_scene)
                            .readBytes()
                            .decodeToString()
                    }
                    MotionLayout(
                        motionScene = MotionScene(content = scene),
                        progress = progress.value,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(
                            modifier = Modifier
                                .layoutId("stickyHeader")
                                .background(MaterialTheme.colorScheme.onTertiaryContainer),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) { Text(text = "Header", style = MaterialTheme.typography.headlineSmall) }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .layoutId("items"),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            userScrollEnabled = isScrollEnable.value
                        ) {
                            items(15) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Item ${it + 1}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

