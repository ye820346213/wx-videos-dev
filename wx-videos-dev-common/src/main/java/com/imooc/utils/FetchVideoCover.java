package com.imooc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FetchVideoCover {

	private String ffmpegEXE;

	public FetchVideoCover(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
    //获取截图
	public void getCover(String videoInputPath,String coverOutputPath)throws Exception{

		//ffmpeg.exe -ss 00:00:01 -y -i test.mp4 -vframes 1 new.jpg
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);

		command.add("-ss");
		command.add("00:00:01");

		command.add("-y");
		command.add("-i");
		command.add(videoInputPath);

		command.add("-vframes");
		command.add("1");

		command.add(coverOutputPath);

		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();

		InputStream errorStream = process.getErrorStream();
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(inputStreamReader);

		String line = "";
		while((line = br.readLine()) != null ){
		}
		if(br != null){
			br.close();
		}
		if(inputStreamReader != null){
			inputStreamReader.close();
		}
		if(errorStream != null){
			errorStream.close();
		}


	}



	public static void main(String[] args) {
		FetchVideoCover ffmpeg = new FetchVideoCover("C:\\ffmpeg\\bin\\ffmpeg.exe");
		try {
			ffmpeg.getCover("E:\\test.mp4","E:\\new.jpg");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
