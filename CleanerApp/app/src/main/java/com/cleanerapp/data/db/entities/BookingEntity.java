package com.cleanerapp.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookings")
public class BookingEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "cleaner_id")
    public int cleanerId;

    @ColumnInfo(name = "service_id")
    public int serviceId;

    @ColumnInfo(name = "cleaner_name")
    public String cleanerName;

    @ColumnInfo(name = "service_type")
    public String serviceType;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "booking_date")
    public long bookingDate;

    @ColumnInfo(name = "status")
    public String status; // PENDING / CONFIRMED / COMPLETED / CANCELLED

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public BookingEntity() {}

    public BookingEntity(int userId, int cleanerId, int serviceId,
                          String cleanerName, String serviceType,
                          double price, long bookingDate, String address) {
        this.userId = userId;
        this.cleanerId = cleanerId;
        this.serviceId = serviceId;
        this.cleanerName = cleanerName;
        this.serviceType = serviceType;
        this.price = price;
        this.bookingDate = bookingDate;
        this.address = address;
        this.status = "PENDING";
        this.createdAt = System.currentTimeMillis();
    }
}
