package com.tensquare.qa.dao;

import com.tensquare.qa.pojo.ViewInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ViewInfoDao extends JpaRepository<ViewInfo,String>, JpaSpecificationExecutor<ViewInfo> {
    @Query(nativeQuery = true,value = "SELECT * FROM v_p_l WHERE CASE WHEN ?1 = '0' THEN lid = lid WHEN ?1 != '0' THEN lid = ?1 END GROUP BY id ORDER BY replytime DESC")
    public Page<ViewInfo> newList(String labelId, Pageable pageable);

    @Query(nativeQuery = true,value = "SELECT * FROM v_p_l WHERE lid = ? GROUP BY id ORDER BY reply DESC")
    public Page<Object> hotList(String labelId, Pageable pageable);

    @Query(nativeQuery = true,value = "SELECT * FROM v_p_l WHERE lid = ? AND solve = 1 GROUP BY id ORDER BY createtime DESC")
    public Page<ViewInfo> waitList(String labelId, Pageable pageable);
}
