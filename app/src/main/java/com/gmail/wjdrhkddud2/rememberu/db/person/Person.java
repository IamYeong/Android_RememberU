package com.gmail.wjdrhkddud2.rememberu.db.person;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.gmail.wjdrhkddud2.rememberu.db.user.User;

@Entity(tableName = "person",
        foreignKeys = {
        @ForeignKey(
                entity = User.class,
                parentColumns = "uid",
                childColumns = "uid",
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
public class Person {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    //uid + name + phone // 특정 유저의 특정 인물이 같은 이름과 같은 번호를 가지고 있다면 같은 사람으로 간주, 정보수정시 해시도 변경.
    @ColumnInfo(name = "hashed")
    private String hashed;

    @ColumnInfo(name = "uid")
    private String uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "birth")
    private long birth;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "gender")
    private char gender;

    @ColumnInfo(name = "bookmark")
    private boolean bookmark;

    @ColumnInfo(name = "description")
    private String description;

    public Person(String hashed) {
        this.hashed = hashed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

}
