package com.imooc.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import com.imooc.enums.VideosStatusEnum;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.Comments;
import com.imooc.pojo.Videos;
import com.imooc.service.BgmService;
import com.imooc.service.VideoService;
import com.imooc.utils.FetchVideoCover;
import com.imooc.utils.MergeVideoMp3;
import com.imooc.utils.PagedResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.imooc.utils.IMoocJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@Api(value = "视频相关业务的接口", tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {

    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐ID", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "视频播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId,
                                  String bgmId, double videoSeconds, int videoWidth, int videoHeight,
                                  String desc,
                                  @ApiParam(value = "短视频", required = true)
                                          MultipartFile file) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户Id不可为空");
        }

        // 文件保存的命名空间
//		String filespace = "E:/wx_videos_dev";
        // 保存在数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";
        String coverPathDB = "/" + userId + "/video";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        String finalVideoPath = "";
        try {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                String fileNamePrefix = fileName.split("\\.")[0];
                if (StringUtils.isNotBlank(fileName)) {
                    //文件上传的最终保存路径
                    finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    //设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";

                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);

                }
            } else {
                return IMoocJSONResult.errorMsg("上传出错");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        //判断bgmId是否为空，若不为空查询bgm信息，合并视频形成新的视频
        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmService.queryBgmById(bgmId);
            String mp3InputPath = FILE_SPACE + bgm.getPath();

            MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
            String videoInputPath = finalVideoPath;
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            String videoCopyName = UUID.randomUUID().toString() + "copy.mp4";
            uploadPathDB = "/" + userId + "/video" + "/" + videoOutputName;
            String uploadPathDB1 = "/" + userId + "/video" + "/" + videoCopyName;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            String finalCopyVideoPath = FILE_SPACE + uploadPathDB1;
            videoInputPath = tool.copyMp4(videoInputPath, finalCopyVideoPath);
            tool.convertor(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
        }
        //对视频进行截图
        FetchVideoCover ffmpeg = new FetchVideoCover(FFMPEG_EXE);
        ffmpeg.getCover(finalVideoPath, FILE_SPACE + coverPathDB);
        //保存视频信息到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoWidth(videoWidth);
        video.setVideoHeight(videoHeight);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideosStatusEnum.SUCCESS.value);
        video.setCreatTime(new Date());
        videoService.saveVideo(video);

        return IMoocJSONResult.ok();
    }

    /**
     * 分页和搜索查询视频列表
     * @param video
     * @param isSaveRecord：1-需要保存 0-不需要保存，或者为空的时候
     * @param page
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/showAll")
    public IMoocJSONResult showAll(@RequestBody Videos video,Integer isSaveRecord,Integer page,Integer pageSize) throws Exception {

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = PAGE_SIZE;
        }

        PagedResult result = videoService.getAllVideos(video,isSaveRecord,page,pageSize);
        return IMoocJSONResult.ok(result);
    }
    @PostMapping(value = "/showMyLike")
    public IMoocJSONResult showMyLike(String userId,Integer page,Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)){
            return IMoocJSONResult.ok();
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 6;
        }

        PagedResult videosList = videoService.queryMyLikeVideos(userId,page,pageSize);
        return IMoocJSONResult.ok(videosList);
    }
    @PostMapping(value = "/showMyFollow")
    public IMoocJSONResult showMyFollow(String userId,Integer page,Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)){
            return IMoocJSONResult.ok();
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 6;
        }

        PagedResult videosList = videoService.queryMyFollowVideos(userId,page,pageSize);
        return IMoocJSONResult.ok(videosList);
    }



    @PostMapping(value = "/hot")
    public IMoocJSONResult hot() throws Exception {
        return IMoocJSONResult.ok(videoService.getHotWords());
    }

    @PostMapping(value = "/userLike")
    public IMoocJSONResult userLike(String userId,String videoId,String videoCreateId) throws Exception {
        videoService.userLikeVideo(userId,videoId,videoCreateId);
        return IMoocJSONResult.ok();
    }

    @PostMapping(value = "/userUnLike")
    public IMoocJSONResult userUnLike(String userId,String videoId,String videoCreateId) throws Exception {
        videoService.userUnLikeVideo(userId,videoId,videoCreateId);
        return IMoocJSONResult.ok();
    }
    @PostMapping(value = "/saveComment")
    public IMoocJSONResult saveComment(@RequestBody Comments comments,String fatherCommentId,String toUserId){

        comments.setFatherCommentId(fatherCommentId);
        comments.setToUserId(toUserId);
        videoService.saveComment(comments);
        return IMoocJSONResult.ok();
    }
    @PostMapping(value = "/getVideoComments")
    public IMoocJSONResult getVideoComments(String videoId,Integer page,Integer pageSize){
        if (StringUtils.isBlank(videoId)){
            return IMoocJSONResult.ok();
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }

        PagedResult List = videoService.getAllComments(videoId,page,pageSize);
        return IMoocJSONResult.ok(List);
    }

}