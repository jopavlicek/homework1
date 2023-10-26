package cz.mendelu.pef.petstore.di

import android.content.Context
import cz.mendelu.pef.petstore.communication.pets.PetsAPI
import cz.mendelu.pef.petstore.communication.pets.PetsRemoteRepositoryImpl
import cz.mendelu.pef.petstore.communication.user.UsersAPI
import cz.mendelu.pef.petstore.communication.user.UsersRemoteRepositoryImpl
import cz.mendelu.pef.petstore.datastore.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun providePetsRepository(api: PetsAPI): PetsRemoteRepositoryImpl
            = PetsRemoteRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideUsersRepository(api: UsersAPI): UsersRemoteRepositoryImpl
            = UsersRemoteRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext appContext: Context): DataStoreRepositoryImpl
            = DataStoreRepositoryImpl(appContext)

}