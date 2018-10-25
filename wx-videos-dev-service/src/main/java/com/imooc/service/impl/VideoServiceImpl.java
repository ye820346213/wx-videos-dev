package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentsVo;
import com.imooc.pojo.vo.VideosVo;
import com.imooc.service.VideoService;
import com.imooc.utils.PagedResult;
import com.imooc.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideosMapper videosMapper;
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private VideosMapperCustom videosMapperCustom;
	@Autowired
	private SearchRecordsMapper searchRecordsMapper;
	@Autowired
	private UsersLikesVideoMapper usersLikesVideoMapper;
	@Autowired
	private CommentsMapper commentsMapper;
	@Autowired
	private CommentsMapperCustom commentsMapperCustom;
	@Autowired
	private Sid sid;

	/**
	 * 保存视频
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveVideo(Videos video) {
		String id = sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);
	}

	/**
	 * 查询videos
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video,Integer isSaveRecord,
									Integer page, Integer pageSize) {
		//保存热搜词
        String desc = video.getVideoDesc();
        String userId = video.getUserId();
        if (isSaveRecord != null && isSaveRecord == 1){
			SearchRecords record = new SearchRecords();
			String recordId = sid.nextShort();
			record.setId(recordId);
			record.setContent(desc);
        	searchRecordsMapper.insert(record);
		}

		PageHelper.startPage(page,pageSize);
		List<VideosVo> list = videosMapperCustom.queryAllVideos(desc,userId);

		PageInfo<VideosVo> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}


	/**
	 * 查询自己收藏的点赞的视频
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId,Integer page,Integer pageSize) {
		PageHelper.startPage(page,pageSize);
		List<VideosVo> list = videosMapperCustom.queryMyLikeVideos(userId);

		PageInfo<VideosVo> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}
	/**
	 * 用户关注过人发表的视频
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId,Integer page,Integer pageSize) {
		PageHelper.startPage(page,pageSize);
		List<VideosVo> list = videosMapperCustom.queryMyFollowVideos(userId);

		PageInfo<VideosVo> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}
	/**
	 * 获取热搜词列表
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotWords() {
         return searchRecordsMapper.getHotWords();
	}
	/**
	 * 用户喜欢点赞
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreateId) {
        System.out.println(videoCreateId);
		//1.保存用户和视频的喜欢点赞关联关系表
		String likeId = sid.nextShort();
        UsersLikesVideo ulv = new UsersLikesVideo();
        ulv.setId(likeId);
		ulv.setUserId(userId);
		ulv.setVideoId(videoId);
		usersLikesVideoMapper.insert(ulv);

		//2.视频喜欢数量累加
		videosMapperCustom.addVideoLikeCount(videoId);
		//3.用户受喜欢数量累加
		usersMapper.addReceiveLikeCount(videoCreateId);
	}
	/**
	 * 用户不喜欢取消点赞
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeVideo(String userId, String videoId, String videoCreateId) {
        //1.删除用户和视频的喜欢点赞关联关系表
		Example example = new Example(UsersLikesVideo.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("videoId",videoId);

		usersLikesVideoMapper.deleteByExample(example);

	    //2.视频喜欢数量累减
		videosMapperCustom.reduceVideoLikeCount(videoId);
	    //3.用户受喜欢数量累减
		usersMapper.reduceReceiveLikeCount(videoCreateId);
   }
	/**
	 * 保存留言
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comments) {
		String id = sid.nextShort();
		comments.setId(id);
		comments.setCreateTime(new Date());
		commentsMapper.insert(comments);

	}
	/**
	 * 查询留言列表
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
		PageHelper.startPage(page,pageSize);

		List<CommentsVo> list = commentsMapperCustom.queryComments(videoId);

		for (CommentsVo c : list) {
			String timeAgo = TimeAgoUtils.format(c.getCreateTime());
			c.setTimeAgoStr(timeAgo);

		}

		PageInfo<CommentsVo> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}
}
