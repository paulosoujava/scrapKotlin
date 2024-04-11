package com.oliveira.scrapy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class BaseEvent {
    LOADING, REGULAR, ERROR, GAMES
}

data class Ui(
    val success: List<DadosTime> = emptyList(),
    val games: List<Match> = emptyList(),
    val title: String = "",
    val year: String = "2024",
    val event: BaseEvent = BaseEvent.LOADING,
    val gameLoading:Boolean = true
)


class MainViewModel:ViewModel() {

    private val _state = MutableStateFlow(Ui())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val (first, second ) =getAllPosition(state.value.year)
            _state.value = Ui(success = first, title = second, event = BaseEvent.REGULAR)
        }
    }

    fun changeYear(year: String) {
        _state.value = Ui(year = year, event = BaseEvent.LOADING)
        viewModelScope.launch {
            val (first, second ) = getAllPosition(year)
            _state.value = Ui(success = first, title = second, event = BaseEvent.REGULAR)
        }
    }

    fun games(year: String) {
        viewModelScope.launch {
            val (games, title ) = getGames(year)
            _state.update {
                it.copy(games = games, title = title,  gameLoading = false)
            }
        }
    }

    fun back() {
        _state.update {
            it.copy(event = BaseEvent.REGULAR)
        }
    }

    fun gamesShow() {
        _state.update {
            it.copy(event = BaseEvent.GAMES)
        }
    }

}