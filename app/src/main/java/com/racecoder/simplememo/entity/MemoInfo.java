package com.racecoder.simplememo.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.racecoder.simplememo.util.Const;

import java.time.ZonedDateTime;

/**
 * 每条便签包含的信息
 */
@Entity(tableName = Const.TABLE_NAME)
public class MemoInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "content")
    public String text;

    @ColumnInfo(name = "update_time")
    public ZonedDateTime updateTime;

    @Ignore
    public MemoInfo(Long id) {
        this.id = id;
    }

    @Ignore
    public MemoInfo(String text, ZonedDateTime updateTime) {
        this.text = text;
        this.updateTime = updateTime;
    }

    public MemoInfo(Long id, String text, ZonedDateTime updateTime) {
        this.id = id;
        this.text = text;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
