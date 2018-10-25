package com.imooc.pojo;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value="用户对象",description="这是用户对象")
public class Users {
	@ApiModelProperty(hidden=true)
	@Id
    @Column(name = "Id")
    private String id;
	 /**
     * 用户名
     */
    @ApiModelProperty(value="用户名", name="username", example="wxuser", required=true)
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value="密码", name="password", example="123456", required=true)
    private String password;
    /**
     * 我的头像，如果没有默认给一张
     */
    @Column(name = "face_image")
    private String faceImage;
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 0-代表女，1-代表男（性别）
     */
    @Column(name = "user_sex")
    private Integer userSex;
    /**
     * 我所在的学校
     */
    @Column(name = "user_school")
    private String userSchool;
    /**
     * 我的粉丝数量
     */
    @ApiModelProperty(hidden=true)
    @Column(name = "fans_counts")
    private Integer fansCounts;

    /**
     * 我关注的人总数
     */
    @ApiModelProperty(hidden=true)
    @Column(name = "follow_counts")
    private Integer followCounts;
    /**
     * 我接受到的赞美/收藏 的数量
     */
    @ApiModelProperty(hidden=true)
    @Column(name = "receive_like_counts")
    private Integer receiveLikeCounts;

    /**
     * @return Id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return face_image
     */
    public String getFaceImage() {
        return faceImage;
    }

    /**
     * @param faceImage
     */
    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取0-代表女，1-代表男
     *
     * @return user_sex - 0-代表女，1-代表男
     */
    public Integer getUserSex() {
        return userSex;
    }

    /**
     * 设置0-代表女，1-代表男
     *
     * @param userSex 0-代表女，1-代表男
     */
    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    /**
     * @return user_school
     */
    public String getUserSchool() {
        return userSchool;
    }

    /**
     * @param userSchool
     */
    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    /**
     * @return fans_counts
     */
    public Integer getFansCounts() {
        return fansCounts;
    }

    /**
     * @param fansCounts
     */
    public void setFansCounts(Integer fansCounts) {
        this.fansCounts = fansCounts;
    }

    /**
     * @return follow_counts
     */
    public Integer getFollowCounts() {
        return followCounts;
    }

    /**
     * @param followCounts
     */
    public void setFollowCounts(Integer followCounts) {
        this.followCounts = followCounts;
    }

    /**
     * @return receive_like_counts
     */
    public Integer getReceiveLikeCounts() {
        return receiveLikeCounts;
    }

    /**
     * @param receiveLikeCounts
     */
    public void setReceiveLikeCounts(Integer receiveLikeCounts) {
        this.receiveLikeCounts = receiveLikeCounts;
    }
}