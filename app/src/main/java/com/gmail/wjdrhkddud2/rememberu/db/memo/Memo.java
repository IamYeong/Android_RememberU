package com.gmail.wjdrhkddud2.rememberu.db.memo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.db.user.User;

@Entity(tableName = "memo",
        foreignKeys = {
        @ForeignKey(
                entity = User.class,
                parentColumns = "uid",
                childColumns = "uid",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Person.class,
                parentColumns = "hashed",
                childColumns = "person_hashed",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
        },
        indices = {
        @Index(
                value = "hashed",
                unique = true
        )
        }

)
public class Memo {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    //uid + person hash + title + yyyy.mm.dd hh:mm:ss
    @ColumnInfo(name = "hashed")
    private String hashed;

    @ColumnInfo(name = "uid")
    private String uid;

    @ColumnInfo(name = "person_hashed")
    private String personHashed;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "create")
    private long create;

    @ColumnInfo(name = "update")
    private long update;

    @ColumnInfo(name = "bookmark")
    private boolean bookmark;

    public Memo(String uid, String personHashed, String hashed) {
        this.personHashed = personHashed;
        this.hashed = hashed;
        this.uid = uid;
    }

    public long getUpdate() {
        return update;
    }

    public void setUpdate(long update) {
        this.update = update;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public String getPersonHashed() {
        return personHashed;
    }

    public void setPersonHashed(String personHashed) {
        this.personHashed = personHashed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}
