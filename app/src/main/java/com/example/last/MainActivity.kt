package com.example.last

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.liveData
import com.example.last.R.string
import com.example.last.ui.theme.LastTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.internal.RegisterListenerMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await


class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var googleSignInClient : GoogleSignInClient

    companion object{
        private val TAG = ""
        private val res = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gA = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gA)

        auth = Firebase.auth

        setContent {
            val currentUser = auth.currentUser
            LastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (currentUser != null){
                        HomePage()
                    }else{
                        Login(auth,{googleSignIN()})
                    }

                }
            }
        }

    }
    private fun googleSignIN(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, res)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==res){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseGoogleAuth(account.idToken!!)
                }catch (e:Exception){
                    Log.d(TAG,"Google Sign In Failed")
                }
            }else{
                Log.d(TAG,exception.toString())
            }

        }
    }
    private fun firebaseGoogleAuth(idToken : String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){
            if (it.isSuccessful){
                Toast.makeText(this,"Login Suceess !!!",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this,"Try again !!!",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
@Composable
fun Login(auth: FirebaseAuth, signInClicked: ()-> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.lig), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(1000.dp))
    }
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back!!!",
            color = Color.White,
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Light,),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 30.dp)
        )
        TextField(
            value = email,

            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 5.dp)
                .fillMaxWidth(),
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null,) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            visualTransformation = VisualTransformation.None,
            singleLine = true,
            onValueChange = { email = it },
//            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.LightGray)



        )
        TextField(
            value = password,
            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 5.dp)
                .fillMaxWidth(),
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null,) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            onValueChange = { password = it }

        )
        Button(
            onClick = {
                try {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            context.startActivity(Intent(context, HomeScreen::class.java))
                        } else {
                            Toast.makeText(context, it.exception!!.message, Toast.LENGTH_SHORT)
                                .show()

                        }
                    }


                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

                }


            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 28.dp, 10.dp, 5.dp),
            border = BorderStroke(1.dp, Color.Green),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
            shape = RoundedCornerShape(13.dp),
        )
        {
            Text(
                text = "Sign In",
                textAlign = TextAlign.Center,
                fontSize = 23.sp,
                modifier = Modifier.padding(5.dp)
            )
        }
        Row() {
            Text(
                "Don't have a account ?",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight,
                color = Color.White,
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)
            )
            TextButton(onClick = {
                context.startActivity(Intent(context, RegisterScreen::class.java))

            }) {
                Text(
                    text = "Sign Up",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Red
                )

            }
        }
        Divider(color = Color.White, thickness = 0.3.dp, modifier = Modifier.padding(15.dp, 20.dp))

        Button(
            onClick = {
                      signInClicked()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp), shape = RectangleShape,
            border = BorderStroke(2.dp, Color.Blue),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
//            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),




        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = "Continue with Google",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(13.dp, 10.dp, 0.dp, 10.dp)
            )

        }


    }

}



///sealed class InputType (
//    val label : String,
//    val icon : ImageVector,
//    val placeholder : String,
//    val visualTransformation: VisualTransformation,
//    val keyboardOption : KeyboardOptions
//    )
//{
//    object Email : InputType(label ="Email", icon = Icons.Default.Person, placeholder = "Enter Email", visualTransformation = VisualTransformation.None, keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next))
//    object Password : InputType(label = "Password", icon = Icons.Default.Lock, placeholder = "Enter Correct Password", visualTransformation = PasswordVisualTransformation(), keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done))
//}
//@Composable
//fun TextInput(inputType: InputType) {
//    var value by remember { mutableStateOf("") }
//
//    TextField(
//        value = value,
//        modifier = Modifier
//            .padding(10.dp, 10.dp, 10.dp, 5.dp)
//            .fillMaxWidth(),
//        label = {Text(text = inputType.label,)},
//        placeholder = {Text(text = inputType.placeholder,)},
//        leadingIcon = {Icon(imageVector = inputType.icon, contentDescription = null,)},
//        keyboardOptions = inputType.keyboardOption,
//        visualTransformation = inputType.visualTransformation,
//        singleLine = true,
//        onValueChange = { value= it }
//
//    )
//}