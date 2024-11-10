package com.rkm.tasky.feature.edit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.rkm.tasky.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private companion object {
        const val TEXT_KEY = "TEXT"
    }
    private val edit = savedStateHandle.toRoute<Destination.Edit>()

    val action = edit.action

    private val _text = MutableStateFlow(savedStateHandle[TEXT_KEY] ?: edit.text)
    val text = _text.asStateFlow()

    fun updateText(text: String) {
        _text.update { text }
        savedStateHandle[TEXT_KEY] = text
    }
}