package com.example.memoscribe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memoscribe.repository.NoteRepository

/**
 * Factory pour créer le NoteViewModel avec le Repository en paramètre.
 *
 * Nécessaire car le ViewModel a besoin d'un Repository qui ne peut pas
 * être injecté automatiquement par le système Android.
 *
 * @param repository Repository à injecter dans le ViewModel
 */
class NoteViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}