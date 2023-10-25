package cz.mendelu.pef.petstore.datastore

interface IDataStoreRepository {
    suspend fun setLoginSuccessful(value: Boolean)
    suspend fun getLoginSuccessful(): Boolean
}