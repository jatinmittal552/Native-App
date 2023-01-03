package com.example.last

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.last.ui.theme.LastTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomePage()

                }
            }
        }
    }
}

@Composable
fun HomePage(){
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = Color.White,
                title = { Text(text = "Last") },
                navigationIcon = {IconButton(onClick = { /*TODO*/ },) {  Icon(Icons.Filled.Menu, contentDescription = "m")
                }},
                actions = {
                    IconButton(onClick =
                    {
                        Firebase.auth.signOut()
                        context.startActivity(Intent(context,MainActivity::class.java))

                    })
                    {
                        Icon(Icons.Filled.Add,contentDescription = null)

                    }
                }

            )
        }
    )
    {


    }

}

