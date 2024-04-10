package com.oliveira.scrapy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class BaseEvent {
    LOADING, REGULAR, ERROR
}

data class Ui(
    val success: List<DadosTime> = emptyList(),
    val title: String = "",
    val event: BaseEvent = BaseEvent.LOADING
)


class MainViewModel:ViewModel() {

    private val _state = MutableStateFlow(Ui())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            //getGames()
            val (first, second ) =getAllPosition()
            _state.value = Ui(first, second, BaseEvent.REGULAR)
        }
    }

}