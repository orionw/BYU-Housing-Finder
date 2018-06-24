package com.orionweller.collegehousing;

import java.util.List;
import com.orionweller.collegehousing.Apartments;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;

@Dao
public interface ApartmentsDao {
    @Query("SELECT * FROM apartments")
    List<Apartments> getAll();

    @Query("SELECT * FROM apartments WHERE uid IN (:userIds)")
    List<Apartments> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM apartments WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    Apartments findByName(String first, String last);

    @Insert
    void insertAll(Apartments... users);

    @Delete
    void delete(Apartments user);
}
