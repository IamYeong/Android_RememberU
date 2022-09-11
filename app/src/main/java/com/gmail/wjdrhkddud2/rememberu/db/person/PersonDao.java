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

    @Query("DELETE FROM PERSON WHERE hashed == :hashed")
    void delete(String hashed);

    @Update
    void update(Person person);

    @Query("SELECT * FROM PERSON WHERE uid = :uid")
    List<Person> selectAll(String uid);

    @Query("SELECT EXISTS(SELECT * FROM PERSON WHERE hashed == :hash)")
    boolean isExist(String hash);

    @Query("SELECT * FROM PERSON WHERE (uid == :uid) AND (hashed == :hashed)")
    Person select(String uid, String hashed);



}
