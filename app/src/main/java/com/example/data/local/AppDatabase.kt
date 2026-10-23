package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.ActivityItem
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.data.model.ChatMessage
import com.example.data.model.Equipment
import com.example.data.model.NotificationItem
import com.example.data.model.PlantScan
import com.example.data.model.SoilReport
import com.example.data.model.UserProfile

class Converters {
    @TypeConverter
    fun fromBookingStatus(status: BookingStatus): String = status.name

    @TypeConverter
    fun toBookingStatus(value: String): BookingStatus = runCatching {
        BookingStatus.valueOf(value)
    }.getOrDefault(BookingStatus.PENDING)
}

@Database(
    entities = [
        UserProfile::class,
        Equipment::class,
        Booking::class,
        PlantScan::class,
        SoilReport::class,
        ActivityItem::class,
        NotificationItem::class,
        ChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): AgriShareDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agrishare_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
