package com.oliveira.scrapy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation

@Composable
fun CardDadosTime(dadosTime: DadosTime) {
    val painter = rememberAsyncImagePainter(dadosTime.img)
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(5f)) {
                    Text("Posição: ${dadosTime.posicao}",
                         fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        dadosTime.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start,

                    )
                }

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "Forest Image",
                        modifier = Modifier
                            .background(Color.White, shape = CircleShape)
                            .clip(CircleShape)
                            .size(40.dp),
                        contentScale = ContentScale.Inside
                    )
                }

            }
            Divider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
            Text("Pontos: ${dadosTime.pontos}",color = Color.White)
            Text("Jogos: ${dadosTime.jogos}",color = Color.White)
            Text("Vitórias: ${dadosTime.vitorias}",color = Color.White)
            Text("Empates: ${dadosTime.empates}",color = Color.White)
            Text("Derrotas: ${dadosTime.derrotas}",color = Color.White)
            Text("Gols Feitos: ${dadosTime.golsFeitos}",color = Color.White)
            Text("Gols Sofridos: ${dadosTime.golsSofridos}",color = Color.White)
            Text("Saldo de Gols: ${dadosTime.saldoGols}",color = Color.White)
            Text("Cartões Amarelos: ${dadosTime.cartoesAmarelos}",color = Color.White)
            Text("Cartões Vermelhos: ${dadosTime.cartoesVermelhos}",color = Color.White)
            Text("Aproveitamento: ${dadosTime.aproveitamento}",color = Color.White)
            Text("Últimos Resultados:", modifier = Modifier.padding(bottom = 6.dp),color = Color.White)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val split = dadosTime.ultimosResultados.split(" ")
                for (i in split) {
                    ResultOfGame(i)
                }
            }

        }
    }
}
@Composable
fun ResultOfGame(result: String, modifier: Modifier = Modifier) {
    val cor = when (result) {
        "V" -> Color(0xFF206B20)
        "E" -> Color.Gray
        "D" -> Color.Red
        else -> Color.Black
    }

    Box(
        modifier = modifier
            .size(30.dp)
            .background(cor, shape = CircleShape)
    ) {
        Text(
            text = result,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
        )
    }
}
