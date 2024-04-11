package com.oliveira.scrapy

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Games(state: State<Ui>, onClick: () -> Unit) {


    if (state.value.gameLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            GifImage()
        }
    } else {
        val groupedGames = state.value.games.groupBy { it.rodada }
        LazyColumn {
            groupedGames.forEach { (rodada, games) ->
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .height(125.dp)
                            .padding(10.dp),
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),

                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconButton(onClick = onClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                            }

                            Text(text = "Rodada $rodada", fontSize = 20.sp, color = Color.White)
                        }
                    }
                }

                games.forEach { game ->
                    item {
                        MatchCard(game)
                    }
                }
            }
        }
    }
}



@Composable
fun MatchCard(match: Match) {
    val painterOne = rememberAsyncImagePainter(match.escudoTimeCasa)
    val painterTwo = rememberAsyncImagePainter(match.escudoTimeVisitante)
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Quando:\n\t ${match.dataHora}", fontSize = 14.sp)
                Text(text = "Jogo:\n\t\t ${match.jogo}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(text = "Onde:\n\tLocal: ${match.local}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(match.link))
                    context.startActivity(intent)
                }) {
                    Text(text = "Detalhe do jogo")
                }
            }


            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterOne,
                    contentDescription = "Forest Image",
                    modifier = Modifier
                        .background(Color.White, shape = CircleShape)
                        .clip(CircleShape)
                        .size(40.dp),
                    contentScale = ContentScale.Inside
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(text = match.timeCasaSigla, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = match.placar ?: "-", fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = match.timeVisitanteSigla, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterTwo,
                    contentDescription = "Forest Image",
                    modifier = Modifier
                        .background(Color.White, shape = CircleShape)
                        .clip(CircleShape)
                        .size(40.dp),
                    contentScale = ContentScale.Inside
                )
            }
        }
    }
}
