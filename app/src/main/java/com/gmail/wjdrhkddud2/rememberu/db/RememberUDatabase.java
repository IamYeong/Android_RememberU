package com.gmail.wjdrhkddud2.rememberu.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gmail.wjdrhkddud2.rememberu.db.memo.Memo;
import com.gmail.wjdrhkddud2.rememberu.db.memo.MemoDao;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.db.person.PersonDao;
import com.gmail.wjdrhkddud2.rememberu.db.user.User;

@Database(entities = {User.class, Person.class, Memo.class}, version = 1)
public abstract class RememberUDatabase extends RoomDatabase {

    private static RememberUDatabase instance;

    public abstract PersonDao personDao();
    public abstract MemoDao memoDao();

    public synchronized static RememberUDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(context, RememberUDatabase.class, "remember_u")
                    /*
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                        }

                        @Override
                        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                            super.onDestructiveMigration(db);
                        }
                    })

                     */

                    //.addMigrations()
                    //.createFromAsset()
                    .build();

            return instance;
        }

        return instance;
    }

}
