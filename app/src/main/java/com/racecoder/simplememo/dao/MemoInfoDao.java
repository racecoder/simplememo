package com.racecoder.simplememo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.racecoder.simplememo.entity.MemoInfo;
import com.racecoder.simplememo.util.Const;

import java.util.List;

@Dao
public interface MemoInfoDao {
    @Query("SELECT * FROM " + Const.TABLE_NAME + " ORDER BY update_time DESC")
    List<MemoInfo> getAll();

    @Query("SELECT * FROM " + Const.TABLE_NAME + " WHERE id = :memoInfoId")
    MemoInfo findById(long memoInfoId);

    /**
     * 保存
     * @return 插入的ID
     */
    @Insert
    long insert(MemoInfo memoInfo);

    /**
     * 更新
     * @return 影响的行数
     */
    @Update
    int updateMemoInfo(MemoInfo memoInfo);

    @Delete
    void delete(MemoInfo memoInfo);
}
