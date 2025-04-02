package com.insanoid.whatsapp.presentation.userregistrationscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insanoid.whatsapp.R

@Composable
@Preview(showSystemUi = true)

fun UserRegistrationScreen(){
    var expanded by remember {
        mutableStateOf(false) }
    var selectedcountry by remember {
        mutableStateOf("Japan")
    }
    var countryCode by remember {
        mutableStateOf("+81")
    }
    var phonenumber by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(48.dp), horizontalAlignment = Alignment.CenterHorizontally)

    {
        Text(text ="Enter your phone number",
            fontSize = 20.sp,
            color= colorResource(id= R.color.dark_green),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Row {
            Text(text= "Whatsapp will need to verify your number")
            Spacer(modifier = Modifier.padding(4.dp))

            Text(text="what's", color= colorResource(R.color.dark_green))
        }
        Text(text = "your number?",color= colorResource(R.color.dark_green))

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.width(230.dp)){
                Text(text = selectedcountry,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Icon(imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = colorResource(R.color.dark_green)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 66.dp),
            thickness = 2.dp,color = colorResource(R.color.dark_green)
        )
        DropdownMenu( expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth() ) {
            listOf("India","USA","Canada","China","Australia").forEach { country->
                DropdownMenuItem(text={Text(text= country) }, onClick = {
                    selectedcountry= country
                    expanded=false

                })
            }
        }
       Column (modifier = Modifier.fillMaxWidth()
           .padding( 16.dp),
           horizontalAlignment = Alignment.CenterHorizontally
       ){

           Row {
               TextField(value = countryCode, onValueChange = {
                   countryCode = it
               }, modifier = Modifier.width(70.dp),
                   textStyle = LocalTextStyle.current.copy( fontSize = 18.sp),
                   colors = TextFieldDefaults.colors(
                       unfocusedContainerColor = Color.Transparent,
                       focusedContainerColor = Color.Transparent,

                       unfocusedIndicatorColor = colorResource(R.color.light_green),
                       focusedIndicatorColor = colorResource(R.color.light_green)
                   )
               )
               Spacer(modifier = Modifier.width(6.dp))

               TextField(value = phonenumber,
                   onValueChange = {
                   phonenumber = it

               }, placeholder = {
                   Text(text = "Phone Number")}, singleLine = true, colors = TextFieldDefaults.colors(
                       unfocusedContainerColor =Color.Transparent,
                       focusedContainerColor = Color.Transparent,
                       unfocusedIndicatorColor = colorResource(R.color.light_green),
                       focusedIndicatorColor = colorResource(R.color.light_green)

                   ))

           }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Carrier charges may apply",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.6f)
            )
           Spacer(modifier = Modifier.height(26.dp))

           Button(onClick = { /*TODO*/ },
                shape = RoundedCornerShape(8.dp),
               colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_green))){
               Text(text = "Next", fontSize = 16.sp)
           }
       }

    }
}

