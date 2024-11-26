package com.rkm.tasky.di.util.image

import com.rkm.tasky.util.image.ImageProcessor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageModule {

    @Singleton
    @Binds
    abstract fun providesImageProcessor(processor: ImageProcessor): ImageProcessor
}