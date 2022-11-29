package com.example.lapaksantri.domain.repositories

import com.example.lapaksantri.domain.entities.Article
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.domain.entities.Slider
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getSliders(): Flow<Resource<List<Slider>>>
    fun getArticles(): Flow<Resource<List<Article>>>
}