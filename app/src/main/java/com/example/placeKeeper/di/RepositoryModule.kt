package com.example.placeKeeper.di

import com.example.placeKeeper.data.repository.CategoryRepositoryImpl
import com.example.placeKeeper.data.repository.PlaceRepositoryImpl
import com.example.placeKeeper.domain.repository.CategoryRepository
import com.example.placeKeeper.domain.repository.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        placeRepositoryImpl: PlaceRepositoryImpl
    ): PlaceRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}