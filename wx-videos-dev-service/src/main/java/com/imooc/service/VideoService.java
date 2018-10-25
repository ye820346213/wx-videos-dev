package com.imooc.service;

import com.imooc.pojo.Bgm;
import com.imooc.pojo.Comments;
import com.imooc.pojo.Videos;
import com.imooc.utils.PagedResult;

import java.util.List;


public interface VideoService  {
	/**
	 * 保存视频
	 */
	void saveVideo(Videos video);
	/**
	 *分页查询视频列表
	 */
    PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page,Integer pageSize);
	/**
	 * 查询自己收藏的点赞的视频
	 */
	PagedResult queryMyLikeVideos(String userId,Integer page,Integer pageSize);
	/**
	 * 用户关注过人发表的视频
	 */
	PagedResult queryMyFollowVideos(String userId,Integer page,Integer pageSize);
    /**
	 * 获取热搜词列表
	 */
	List<String> getHotWords();
	/**
	 * 用户喜欢点赞
	 */
	void userLikeVideo(String userId,String videoId,String videoCreateId);
	/**
	 * 用户不喜欢取消点赞
	 */
	void userUnLikeVideo(String userId,String videoId,String videoCreateId);
	/**
	 * 保存留言
	 */
	void saveComment(Comments comments);
	/**
	 * 查询留言列表
	 */
	PagedResult getAllComments(String videoId,Integer page,Integer pageSize);



}
