package com.example.diceroller

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
    }
}

@Composable
fun DiceRollerApp() {

    // Mengambil context Android
    val context = LocalContext.current

    // Menyiapkan sound dadu
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.dice_roll)
    }

    // Membersihkan MediaPlayer ketika aplikasi/komponen ditutup
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    // Nilai dadu pertama
    var diceNumber1 by remember {
        mutableStateOf(1)
    }

    // Nilai dadu kedua
    var diceNumber2 by remember {
        mutableStateOf(1)
    }

    // Nilai rotasi dadu
    var rotation by remember {
        mutableStateOf(0f)
    }

    // Animasi rotasi
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(durationMillis = 500),
        label = "Dice Rotation"
    )

    // Menyimpan riwayat kocokan
    val history = remember {
        mutableStateListOf<String>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Judul
        Text(
            text = "Dice Roller",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Menampilkan dua buah dadu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Dadu pertama
            Image(
                painter = painterResource(
                    id = getDiceImage(diceNumber1)
                ),
                contentDescription = "Dadu pertama",
                modifier = Modifier
                    .size(150.dp)
                    .rotate(animatedRotation)
            )

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            // Dadu kedua
            Image(
                painter = painterResource(
                    id = getDiceImage(diceNumber2)
                ),
                contentDescription = "Dadu kedua",
                modifier = Modifier
                    .size(150.dp)
                    .rotate(animatedRotation)
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Menampilkan total kedua dadu
        Text(
            text = "Total: ${diceNumber1 + diceNumber2}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Tombol kocok dadu
        Button(
            onClick = {

                // Memutar sound
                mediaPlayer?.seekTo(0)
                mediaPlayer?.start()

                // Mengacak dadu pertama
                diceNumber1 = Random.nextInt(1, 7)

                // Mengacak dadu kedua
                diceNumber2 = Random.nextInt(1, 7)

                // Menambahkan rotasi
                rotation += 360f

                // Menambahkan hasil ke riwayat
                history.add(
                    "Dadu 1: $diceNumber1 | Dadu 2: $diceNumber2 | Total: ${diceNumber1 + diceNumber2}"
                )
            }
        ) {
            Text(
                text = "Kocok Dadu"
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Judul riwayat
        Text(
            text = "Riwayat Kocokan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // Daftar riwayat
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                history.reversed()
            ) { result ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// Fungsi untuk menentukan gambar dadu
fun getDiceImage(number: Int): Int {

    return when (number) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
}