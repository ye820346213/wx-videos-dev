package com.imooc.enums;

public enum VideosStatusEnum {

    SUCCESS(1), //发布成功
    FORBID(2); //禁止播放，管理员操作

    public final int value;

    VideosStatusEnum(int value){
        this.value = value;
    }

    public  int getValue(){
        return value;
    }
}
