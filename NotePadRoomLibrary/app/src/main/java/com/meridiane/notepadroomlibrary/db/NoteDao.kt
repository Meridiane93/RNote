package com.meridiane.notepadroomlibrary.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert // insert Entity
    suspend fun insertEntity(entity: Entity)

    @Query("SELECT * FROM note")
    fun getAllEntity(): Flow<List<Entity>>

    @Update // update Entity
    suspend fun updateEntity(entity: Entity)

    @Query("DELETE FROM note WHERE id IS :id")
    suspend fun deleteEntity(id:Int)

    @Query("SELECT * FROM note WHERE dateString LIKE :dateString") // get Entity by date
    fun getAllEntityByDate(dateString:String): Flow<List<Entity>>
}