package me.edujtm.todoexample.di

import androidx.room.Room
import me.edujtm.todoexample.domain.TodoMediator
import me.edujtm.todoexample.infra.api.TodoApiService
import me.edujtm.todoexample.infra.persistence.AppDatabase
import me.edujtm.todoexample.ui.todoform.TodoFormViewModel
import me.edujtm.todoexample.ui.todolist.TodoViewModel
import okhttp3.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val todoModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "todo-app-database"
        ).build()
    }

    single {
        val db = get<AppDatabase>()
        db.todoDao()
    }

    single {
        OkHttpClient.Builder()
            .connectionSpecs(listOf(
                ConnectionSpec.MODERN_TLS,
                ConnectionSpec.COMPATIBLE_TLS,
                ConnectionSpec.CLEARTEXT))
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        val retrofit = get<Retrofit>()
        retrofit.create(TodoApiService::class.java)
    }

    factory {
        TodoMediator(get(), get())
    }

    viewModel {
        TodoViewModel(get())
    }

    viewModel {
        TodoFormViewModel(get())
    }
}