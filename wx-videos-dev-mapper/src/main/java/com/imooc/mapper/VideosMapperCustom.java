package com.imooc.mapper;

import com.imooc.pojo.Videos;
import com.imooc.pojo.vo.VideosVo;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

     List<VideosVo> queryAllVideos(@Param("videoDesc") String videoDesc,
                                   @Param("userId") String userId);
    /**
     * 查询关注的视频
     * @param userId
     */
    List<VideosVo> queryMyFollowVideos(@Param("userId") String userId);
    /**
     * 查询点赞视频
     * @param userId
     */
    List<VideosVo> queryMyLikeVideos(@Param("userId") String userId);
    /**
     * 对视频喜欢的数量进行累加
     * @param videoId
     */
    void addVideoLikeCount(String videoId);
    /**
     * 对视频喜欢的数量进行累减
     * @param videoId
     */
    void reduceVideoLikeCount(String videoId);
}