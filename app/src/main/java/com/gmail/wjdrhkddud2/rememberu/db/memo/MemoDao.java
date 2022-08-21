package com.gmail.wjdrhkddud2.rememberu.db.memo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoDao {

    @Insert
    void insert(Memo memo);

    @Delete
    void delete(Memo memo);

    @Update
    void update(Memo memo);

    @Query("SELECT * FROM MEMO WHERE uid = :uid")
    List<Memo> selectAll(String uid);

    @Query("SELECT * FROM MEMO WHERE person_hashed == :personHashed")
    List<Memo> selectByPerson(String personHashed);

}
