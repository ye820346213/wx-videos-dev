package com.imooc.mapper;

import com.imooc.pojo.Videos;
import com.imooc.pojo.vo.CommentsVo;
import com.imooc.pojo.vo.VideosVo;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Videos> {

     List<CommentsVo> queryComments(@Param("videoId") String videoId);
}