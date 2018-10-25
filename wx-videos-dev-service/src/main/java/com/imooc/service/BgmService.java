package com.imooc.service;

import java.util.List;

import com.imooc.pojo.Bgm;


public interface BgmService {
	/**
	 * 查询背景音乐列表
	 */
	List<Bgm> queryBgmList();
	/**
	 * 根据bgmId查询背景音乐
	 */
	Bgm queryBgmById(String bgmId);
}
