package com.example.collapsingtoolbarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collapsingtoolbarapp.ui.theme.CollapsingToolbarAppTheme

private val MinToolbarHeight = 0.dp
private val MaxToolbarHeight = 200.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollapsingToolbarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeToolbar()
                }
            }
        }
    }
}

@Composable
fun HomeToolbar() {
    CollapsingToolbarLayout(
        minToolbarHeight = MinToolbarHeight,
        maxToolbarHeight = MaxToolbarHeight,
        lazyListScope = {
            items(100) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color.LightGray),
                    text = "Text number $it",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        },
        toolbar = {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.messi),
                contentDescription = null
            )
        }
    )
}