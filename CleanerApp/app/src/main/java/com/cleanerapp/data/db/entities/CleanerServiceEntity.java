package com.cleanerapp.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "cleaner_services",
    foreignKeys = @ForeignKey(
        entity = UserEntity.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class CleanerServiceEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "cleaner_name")
    public String cleanerName;

    @ColumnInfo(name = "service_type")
    public String serviceType;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "is_available")
    public boolean isAvailable;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "rating")
    public float rating;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public CleanerServiceEntity() {}

    public CleanerServiceEntity(int userId, String cleanerName, String serviceType,
                                 String description, double price,
                                 double latitude, double longitude, String phone) {
        this.userId = userId;
        this.cleanerName = cleanerName;
        this.serviceType = serviceType;
        this.description = description;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isAvailable = true;
        this.phone = phone;
        this.rating = 4.0f;
        this.createdAt = System.currentTimeMillis();
    }
}
