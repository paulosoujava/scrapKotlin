package com.oliveira.scrapy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import com.oliveira.scrapy.ui.theme.ScrapyTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = MainViewModel()
            val state = viewModel.state.collectAsState()
            val anos = listOf(
                "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020",
                "2021", "2022", "2023", "2024"
            )
            val selectedAno = remember { mutableStateOf(anos[anos.size - 1]) }

            ScrapyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    // Games(viewModel, "2023")


                    when (state.value.event) {
                        BaseEvent.LOADING -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                GifImage()
                            }
                        }

                        BaseEvent.ERROR -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Ops, Falha ao recuperar dados do site. Tente novamente mais tarde")

                            }
                        }

                        BaseEvent.GAMES -> {
                            Games( state){
                                viewModel.back()
                            }
                        }

                        BaseEvent.REGULAR -> {
                            LazyColumn {
                                stickyHeader {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Black)
                                            .height(125.dp)
                                            .padding(10.dp),
                                    ) {

                                        Text(text = state.value.title, fontSize = 18.sp, color = Color.White)
                                        Divider()
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 10.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Button(onClick = {
                                                viewModel.games(selectedAno.value)
                                                viewModel.gamesShow()
                                            }) {
                                                Text(text = "Rodadas")
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            AnoDropdownMenu(
                                                viewModel = viewModel,
                                                anos = anos,
                                                selectedAno = selectedAno
                                            )
                                        }

                                    }

                                }
                                state.value.success.forEach { dadosTime ->
                                    item {
                                        CardDadosTime(dadosTime)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnoDropdownMenu(viewModel: MainViewModel, anos: List<String>, selectedAno: MutableState<String>) {

    val dropdownMenuState = remember { mutableStateOf(false) }
    Column {
        OutlinedButton(
            onClick = { dropdownMenuState.value = true },
        ) {
            Text(selectedAno.value, color = Color.White, fontSize = 18.sp)
        }

        DropdownMenu(
            expanded = dropdownMenuState.value,
            onDismissRequest = { dropdownMenuState.value = false }
        ) {
            anos.forEach { ano ->
                DropdownMenuItem(
                    onClick = {
                        selectedAno.value = ano
                        viewModel.changeYear(ano)
                        dropdownMenuState.value = false
                    },
                    text = { Text(ano, color = Color.Black, fontSize = 18.sp) }
                )
            }
        }
    }
}

@Composable
fun htmlText(html: String) = buildAnnotatedString {
    val htmlText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    append(htmlText)

}
