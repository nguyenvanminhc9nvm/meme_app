package com.minhnv.meme_app.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minhnv.meme_app.utils.Constants

@Entity(tableName = Constants.TB_COMMUNITIES)
data class Communities(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "idString")
    val idString: String
)
