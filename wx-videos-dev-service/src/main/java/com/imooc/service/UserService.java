package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.UsersReport;

public interface UserService {
	/**
	 * 判断用户名是否存在
	 * @param username
	 * @return boolean
	 */
	boolean queryUsernameIsExist(String username);
	/**
	 * 保存用户
	 * @param user
	 * @return void
	 */
	 void saveUser(Users user);
	/**
	 * 用户登录，根据用户名和密码查询用户
	 * @param username,password
	 * @return Users
	 */
	 Users queryUserForLogin(String username, String password);
	/**
	 * 用户修改信息
	 * @param user
	 * @return void
	 */
	 void updateUserInfo(Users user);
	/**
	 * 查询用户信息
	 * @param userId
	 * @return Users
	 */
	 Users queryUserInfo(String userId);
	/**
	 * 判断用户和视频的点赞关系
	 * @param userId,videoId
	 * @return boolean
	 */
	boolean isUserLikeVideo(String userId,String videoId);
	/**
	 * 增加用户与粉丝的关系
	 * @param userId,videoId
	 * @return void
	 */
	void saveUserFanRelation(String userId,String fanId);
	/**
	 * 删除用户和粉丝的关系
	 * @param userId,videoId
	 * @return void
	 */
	void deleteUserFanRelation(String userId,String fanId);
	/**
	 * 判断用户是否关注
	 * @param userId,videoId
	 * @return boolean
	 */
	boolean queryisFollow(String userId,String fanId);
	/**
	 * 保存举报信息
	 * @param
	 * @return boolean
	 */
	void reportUser(UsersReport usersReport);
}
