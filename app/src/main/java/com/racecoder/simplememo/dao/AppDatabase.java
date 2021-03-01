package com.racecoder.simplememo.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.racecoder.simplememo.entity.MemoInfo;
import com.racecoder.simplememo.util.Const;

@Database(entities = {MemoInfo.class}, version = Const.DATABASE_VERSION)
@TypeConverters({TimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    /**
     * 获取MemoInfo数据库操作接口
     */
    public abstract MemoInfoDao getMemoInfoDao();

}
