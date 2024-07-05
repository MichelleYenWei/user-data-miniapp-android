package com.tawk.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
class AppModule {
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory
}
//
//@Module
//abstract class CurrencyViewModelModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(CurrencyViewModelImpl::class)
//    abstract fun bindCurrencyViewModel(viewModel: CurrencyViewModelImpl): ViewModel
//}
//
//@Module
//class RepositoryModule {
//    @Provides
//    fun provideCurrencyRepository(
//        currencyInfoDataAccess: CurrencyInfoDataAccess,
//        context: Context
//    ): CurrencyRepository {
//        return CurrencyRepositoryImpl(currencyInfoDataAccess, context)
//    }
//}
//
//@Module
//class CurrencyInfoDataAccessModule {
//    @Provides
//    fun provideCurrencyInfoDataAccess(database: AppDatabase): CurrencyInfoDataAccess {
//        return database.CurrencyInfoDataAccess()
//    }
//}
//
//@Module
//class DatabaseModule {
//    @Singleton
//    @Provides
//    fun provideAppDatabase(application: Application): AppDatabase {
//        return AppDatabase.getDatabase(application)
//    }
//}


class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("Unknown model class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)