package com.cleanerapp.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "service_categories")
public class ServiceCategoryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "icon_name")
    public String iconName;

    @ColumnInfo(name = "is_active")
    public boolean isActive;

    public ServiceCategoryEntity() {}

    public ServiceCategoryEntity(String name, String iconName) {
        this.name = name;
        this.iconName = iconName;
        this.isActive = true;
    }
}
