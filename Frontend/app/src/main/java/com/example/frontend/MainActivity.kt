package com.example.frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setContent { 
            Greeting(name = "hello, jetpack compose!")
        }
    }

    @Composable
    fun Greeting(name: String){
        Column{
            Text(text = "hello, $name!")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GeetingPreview(){
        Greeting("")
    }
}