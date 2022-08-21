package com.gmail.wjdrhkddud2.rememberu.db.person;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PersonDao {

    @Insert
    void insert(Person person);

    @Delete
    void delete(Person person);

    @Update
    void update(Person person);

    @Query("SELECT * FROM PERSON")
    List<Person> selectAll();



}
