package com.imooc.mapper;

import com.imooc.pojo.Users;
import com.imooc.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

    /**
     * 用户受到喜欢的数量进行累加
     * @param userId
     */
    void addReceiveLikeCount(String userId);
    /**
     * 用户受到喜欢的数量进行累减
     * @param userId
     */
    void reduceReceiveLikeCount(String userId);

    /**
     * 用户粉丝数量进行累加
     * @param userId
     */
    void addFansCount(String userId);

    /**
     * 用户粉丝数量进行累减
     * @param userId
     */
    void reduceFansCount(String userId);

    /**
     * 用户关注数量进行累加
     * @param userId
     */
    void addFollersCount(String userId);

    /**
     * 用户关注数量进行累减
     * @param userId
     */
    void reduceFollersCount(String userId);




}