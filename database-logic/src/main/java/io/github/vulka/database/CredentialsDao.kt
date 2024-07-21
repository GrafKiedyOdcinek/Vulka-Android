package io.github.vulka.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CredentialsDao {
    @Insert
    suspend fun insert(credentials: Credentials)

    @Update
    suspend fun update(credentials: Credentials)

    @Delete
    fun delete(credentials: Credentials)

    @Query("SELECT COUNT(*) FROM credentials")
    fun count(): Int

    @Query("SELECT * FROM credentials LIMIT 1")
    fun get(): Credentials?

    @Query("SELECT * FROM credentials WHERE id=:id LIMIT 1")
    fun getById(id: UUID): Flow<Credentials?>

    @Query("SELECT * FROM credentials")
    fun getAll(): List<Credentials>
}
