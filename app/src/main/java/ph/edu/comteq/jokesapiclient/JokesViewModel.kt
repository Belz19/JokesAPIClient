package ph.edu.comteq.jokesapiclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class JokesUiState{
    object Idle: JokesUiState() //initial state
    object Loading: JokesUiState()
    data class Success(val jokes: List<Joke>): JokesUiState() //data fetched successfully
    data class Error(val message: String): JokesUiState()
}

class JokesViewModel: ViewModel (){
    private val api = RetrofitInstance.jokeAPI

    //private: only viewmodel can change
    private val _uiState = MutableStateFlow<JokesUiState>(JokesUiState.Idle)

    val uiState: StateFlow<JokesUiState> = _uiState.asStateFlow()

    //fetch Jokes
    fun getJokes(){
        viewModelScope.launch {
            _uiState.value = JokesUiState.Loading
            try {
                val jokes = api.getJokes()
                _uiState.value = JokesUiState.Success(jokes)
            }catch (e:Exception){
                _uiState.value = JokesUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    fun addJoke(setup: String, punchline: String){
        viewModelScope.launch {
            try {
                val newJoke = Joke(id = null, setup = setup, punchline = punchline)
                api.addJoke(newJoke)
                getJokes()
            }catch (e: java.lang.Exception){
                _uiState.value = JokesUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    fun deleteJoke(id: Int){
        viewModelScope.launch {
            try {
                api
                    .deleteJoke(id)
                getJokes()
            }catch (e: Exception){
                _uiState.value = JokesUiState.Error(
                    "Failed to delete joke: ${e.message ?: " Unknown error"}"
                )
            }
        }
    }
    fun updateJoke(id: Int, setup: String,punchline: String){
        viewModelScope.launch {
            try {
                val updatedJoke = Joke(id = id, setup = setup, punchline = punchline)
                api.updateJoke(id, updatedJoke)
                getJokes()
            }catch (e: Exception){
                _uiState.value = JokesUiState.Error(
                    "Failed to update Joke: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }
}