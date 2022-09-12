package com.gmail.wjdrhkddud2.rememberu.db.memo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Memo memo);

    @Delete
    void delete(Memo memo);

    @Query("DELETE FROM MEMO WHERE hashed == :hashed")
    void delete(String hashed);

    @Update
    void update(Memo memo);

    @Query("SELECT * FROM MEMO WHERE hashed == :hash")
    Memo select(String hash);

    @Query("SELECT * FROM MEMO WHERE uid = :uid")
    List<Memo> selectAll(String uid);

    @Query("SELECT * FROM MEMO WHERE person_hashed == :personHashed")
    List<Memo> selectByPerson(String personHashed);

    @Query("SELECT * FROM MEMO WHERE (uid == :uid) AND (person_hashed == :personHashed)")
    List<Memo> selectByUserAndPerson(String uid, String personHashed);

}
