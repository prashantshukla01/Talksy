package com.insanoid.whatsapp.presentation.Callscreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insanoid.whatsapp.R


@Composable
@Preview(showSystemUi = true)
fun FavoritesScreen(){
    val sampleFavorites = listOf(
        FavoriteContact(image = R.drawable.rashmika, name = "Rashmika"),
        FavoriteContact(image = R.drawable.sharukhkhan, name = "Shahrukh Khan"),
        FavoriteContact(image = R.drawable.mrbeast, name = "Mr Beast"),
        FavoriteContact(image = R.drawable.bhuvan_bam, name = "Bhuvan Bam"),
        FavoriteContact(image = R.drawable.akshay_kumar, name = "Akshay Kumar")
    )
    Column {
        Text(
            text = "Favorites",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)

        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())) {
                sampleFavorites.forEach {
                    FavoritesItem(it)
                }

        }

    }
}
data class FavoriteContact(val image: Int, val name: String)