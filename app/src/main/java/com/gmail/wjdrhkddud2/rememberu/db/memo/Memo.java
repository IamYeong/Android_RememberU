package com.gmail.wjdrhkddud2.rememberu.db.memo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.gmail.wjdrhkddud2.rememberu.db.person.Person;

@Entity(tableName = "memo",
        foreignKeys = {
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

    @ColumnInfo(name = "person_hashed")
    private String personHashed;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "create")
    private long create;

    @ColumnInfo(name = "bookmark")
    private boolean bookmark;

    public Memo(String hashed, String personHashed) {
        this.personHashed = personHashed;
        this.hashed = hashed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
