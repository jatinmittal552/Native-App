package com.example.last

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.last.ui.theme.LastTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterScreen : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            LastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Register(auth)
                }
            }
        }
    }
}

@Composable
fun Register(auth: FirebaseAuth){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.background), contentDescription = null, contentScale = ContentScale.FillHeight, modifier = Modifier.size(1000.dp))
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Welcome !!!", color = Color.White, fontSize = 35.sp, fontWeight = FontWeight.ExtraLight,modifier = Modifier.padding(0.dp,0.dp,0.dp,24.dp))
//
        TextField(
            value =  name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 6.dp),
            label = { Text(text = "Name")},
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            visualTransformation = VisualTransformation.None,
            onValueChange = { name= it }
        )

        TextField(
            value = email,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 6.dp),
            label = { Text(text = "Email")},
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            visualTransformation = VisualTransformation.None,
            onValueChange = { email= it }
        )
        TextField(
            value =  password,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 6.dp),
            label = { Text(text = "Password")},
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password= it }
        )
//        TextInput(ScriptGroup.Input.Email)
//        TextInput(ScriptGroup.Input.Password)
        Button(onClick = {
            try {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    if (it.isSuccessful){
                        context.startActivity(Intent(context,HomeScreen::class.java))
                    }
                    else{
                        Toast.makeText(context,it.exception!!.message,Toast.LENGTH_SHORT).show()
                    }
                }

            }catch (e :Exception){
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp, 25.dp, 14.dp, 10.dp),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(2.dp, Color.Red),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
//            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)

        )
        {
            Text(text = "Sign Up", fontSize = 23.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(6.dp) )
        }
        Divider(color = Color.White, thickness = .19.dp, modifier = Modifier.padding(19.dp))
        Row() {
            Text(text = "Already a User ?", color = Color.White, fontSize = 14.sp , fontWeight = FontWeight.ExtraLight, modifier = Modifier.padding(0.dp,15.dp))
            TextButton(onClick = {
                context.startActivity(Intent(context,MainActivity::class.java))

            }) {
                Text(text = "Sign In", color = Color.Green, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            }

        }
    }

}
//sealed class Input(
//    val label : String,
////    val policyholder : String,
//    val icon : ImageVector,
//    val keyboardOptions: KeyboardOptions,
//    val visualTransformation: VisualTransformation
//
//)
//{
//    object Name : Input(label = "Full Name", icon = Icons.Default.Person, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), visualTransformation = VisualTransformation.None)
//    object Email : Input(label = "Email", icon = Icons.Default.Email,keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next), visualTransformation = VisualTransformation.None)
//    object Password : Input(label = "Password", icon = Icons.Default.Lock, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), visualTransformation = PasswordVisualTransformation())
//}
//@Composable
//fun TextInput(input: ScriptGroup.Input){
//    var value by remember { mutableStateOf("") }
//
//    TextField(
//        value = value ,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(12.dp, 6.dp),
//        label = { Text(text = input.label)},
////        placeholder = { Text(text = input.)},
//        leadingIcon = { Icon(imageVector = input.icon, contentDescription = null) },
//        keyboardOptions = input.keyboardOptions,
//        visualTransformation = input.visualTransformation,
//        onValueChange = { value= it }
//    )
//}


