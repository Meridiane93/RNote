package com.meridiane.notepadroomlibrary.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insertEntity(entity: Entity)

    @Query("SELECT * FROM note")
    fun getAllEntity(): Flow<List<Entity>>

    @Update // update NoteItem
    suspend fun updateEntity(entity: Entity)

    @Query("DELETE FROM note WHERE id IS :id")
    suspend fun deleteEntity(id:Int)

    @Query("SELECT * FROM note WHERE dateString LIKE :dateString")
    fun getAllEntityByDate(dateString:String): Flow<List<Entity>>
}