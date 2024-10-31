package com.rkm.tasky.di.util.validator

import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator
import com.rkm.tasky.util.validator.implementation.EmailPatternValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class EmailValidatorModule {

    @Binds
    abstract fun bindsEmailValidator(validator: EmailPatternValidatorImpl): EmailPatternValidator
}