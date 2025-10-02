package com.example.videoplayerhub.config

import android.content.Context
import androidx.room.*
import com.example.videoplayerhub.model.FavoritePhoto
import com.example.videoplayerhub.repository.FavoritePhotoDao

@Database(
    entities = [FavoritePhoto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritePhotoDao(): FavoritePhotoDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "video-player-hub.db"
                )
                    // DEMO: izinkan query di main thread.
                    // Produksi sebaiknya pakai coroutine atau RxJava.
                    .allowMainThreadQueries()
                    .build().also { INSTANCE = it }
            }
    }
}
