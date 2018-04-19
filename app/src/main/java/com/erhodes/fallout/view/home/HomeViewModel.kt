package com.erhodes.fallout.view.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.erhodes.fallout.MyApplication
import com.erhodes.fallout.model.Character

class HomeViewModel : ViewModel() {
    val characterRepository = MyApplication.getComponent().characterRepo

    fun getCharacters() : LiveData<List<Character>> {
        return characterRepository.charactersLiveData
    }

    fun resetDatabase() {
        characterRepository.resetDatabase()
    }
}