package com.imooc.service.impl;

import com.imooc.mapper.UsersFansMapper;
import com.imooc.mapper.UsersLikesVideoMapper;
import com.imooc.mapper.UsersReportMapper;
import com.imooc.pojo.UsersFans;
import com.imooc.pojo.UsersLikesVideo;
import com.imooc.pojo.UsersReport;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersMapper userMapper;
	@Autowired
	private UsersFansMapper usersFansMapper;
	@Autowired
	private UsersLikesVideoMapper usersLikesVideoMapper;
	@Autowired
	private UsersReportMapper usersReportMapper;
	@Autowired
	private Sid sid;
	
	//判断用户名是否存在
	@Transactional(propagation= Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String username) {
		Users user = new Users();
		user.setUsername(username);
		Users result = userMapper.selectOne(user);
		
		return result == null ?false : true;
	}
	//保存用户
    @Transactional(propagation= Propagation.REQUIRED)
	@Override
	public void saveUser(Users user) {
		String userId = sid.nextShort();
		user.setId(userId);
		userMapper.insert(user);

	}
    //用户登录，根据用户名和密码查询用户
    @Transactional(propagation= Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String password) {
		
    	Example userExample = new Example(Users.class);
    	Criteria criteria = userExample.createCriteria();
    	criteria.andEqualTo("username",username);
    	criteria.andEqualTo("password",password);
    	Users result = userMapper.selectOneByExample(userExample);
    	
    	return result;
	}
    //用户修改信息
	@Transactional(propagation= Propagation.REQUIRED)
	@Override
	public void updateUserInfo(Users user) {
		Example userExample = new Example(Users.class);
    	Criteria criteria = userExample.createCriteria();
    	criteria.andEqualTo("id",user.getId());
    	userMapper.updateByExampleSelective(user, userExample);
	}
	//查询用户信息
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserInfo(String userId) {
		Example userExample = new Example(Users.class);
    	Criteria criteria = userExample.createCriteria();
    	criteria.andEqualTo("id",userId);
    	Users result = userMapper.selectOneByExample(userExample);
		return result;
	}
    //判断用户和视频的点赞关系
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeVideo(String userId, String videoId) {

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)){
		   return false;
		}

		Example example = new Example(UsersLikesVideo.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("videoId",videoId);

		List<UsersLikesVideo> list = usersLikesVideoMapper.selectByExample(example);
		if (list != null && list.size() > 0){
			return  true;
		}
		return false;
	}
	//增加用户与粉丝的关系
	@Transactional(propagation= Propagation.REQUIRED)
	@Override
	public void saveUserFanRelation(String userId, String fanId) {
		//1.保存用户与粉丝关系
		String relId = sid.nextShort();
		UsersFans usersFans = new UsersFans();
		usersFans.setId(relId);
		usersFans.setUserId(userId);
		usersFans.setFanId(fanId);
		usersFansMapper.insert(usersFans);
		//2.增加被关注用户粉丝数
		userMapper.addFansCount(userId);
		//3.增加关注用户的关注数
		userMapper.addFollersCount(fanId);
	}
	//删除用户和粉丝的关系
	@Transactional(propagation= Propagation.REQUIRED)
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {
		//1.删除用户与粉丝关系
		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("fanId",fanId);

		usersFansMapper.deleteByExample(example);
		//2.减少被关注用户粉丝数
		userMapper.reduceFansCount(userId);
		//3.减少关注用户的关注数
		userMapper.reduceFollersCount(fanId);

	}
	//判断用户是否关注
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryisFollow(String userId, String fanId) {

		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("fanId",fanId);

		List<UsersFans> list = usersFansMapper.selectByExample(example);
		if (list != null && list.size() > 0 && !list.isEmpty()){
			return  true;
		}

		return false;
	}
	//保存举报信息
	@Transactional(propagation= Propagation.REQUIRED)
	@Override
	public void reportUser(UsersReport usersReport) {
		String urId = sid.nextShort();
		usersReport.setId(urId);
		usersReport.setCreateDate(new Date());

		usersReportMapper.insert(usersReport);
	}
}
