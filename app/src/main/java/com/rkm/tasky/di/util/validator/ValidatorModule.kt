package com.rkm.tasky.di.util.validator

import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator
import com.rkm.tasky.util.validator.abstraction.NamePatternValidator
import com.rkm.tasky.util.validator.abstraction.PasswordPatternValidator
import com.rkm.tasky.util.validator.implementation.EmailPatternValidatorImpl
import com.rkm.tasky.util.validator.implementation.NamePatternValidatorImpl
import com.rkm.tasky.util.validator.implementation.PasswordPatternValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ValidatorModule {

    @Binds
    abstract fun bindsEmailValidator(validator: EmailPatternValidatorImpl): EmailPatternValidator

    @Binds
    abstract fun bindsPasswordValidator(validator: PasswordPatternValidatorImpl): PasswordPatternValidator

    @Binds
    abstract fun bindsNameValidator(validator: NamePatternValidatorImpl): NamePatternValidator
}