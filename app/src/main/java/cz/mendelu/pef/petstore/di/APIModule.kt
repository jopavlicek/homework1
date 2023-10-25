package cz.mendelu.pef.petstore.di

import cz.mendelu.pef.petstore.communication.pets.PetsAPI
import cz.mendelu.pef.petstore.communication.user.UsersAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun providePetsAPI(retrofit: Retrofit): PetsAPI
        = retrofit.create(PetsAPI::class.java)

    @Provides
    @Singleton
    fun provideUsersAPI(retrofit: Retrofit): UsersAPI
        = retrofit.create(UsersAPI::class.java)

}