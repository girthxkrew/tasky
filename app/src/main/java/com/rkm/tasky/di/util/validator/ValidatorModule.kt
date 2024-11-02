package com.rkm.tasky.di.util.validator

import com.rkm.tasky.util.validator.abstraction.Validator
import com.rkm.tasky.util.validator.implementation.EmailPatternValidator
import com.rkm.tasky.util.validator.implementation.NamePatternValidator
import com.rkm.tasky.util.validator.implementation.PasswordPatternValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class EmailValidator

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class NameValidator

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class PasswordValidator

@Module
@InstallIn(ViewModelComponent::class)
abstract class ValidatorModule {

    @Binds
    @EmailValidator
    abstract fun bindsEmailValidator(validator: EmailPatternValidator): Validator

    @Binds
    @PasswordValidator
    abstract fun bindsPasswordValidator(validator: PasswordPatternValidator): Validator

    @Binds
    @NameValidator
    abstract fun bindsNameValidator(validator: NamePatternValidator): Validator
}