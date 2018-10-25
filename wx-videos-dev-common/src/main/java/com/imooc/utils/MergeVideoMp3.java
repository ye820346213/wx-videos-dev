package com.imooc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

	private String ffmpegEXE;

	public MergeVideoMp3(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
    //去除原文件的背景音轨
	public String copyMp4(String videoInputPath,String videoOutputPath)throws Exception{

		//ffmpeg -i video.avi -vcodec copy -an video2.avi
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);

		command.add("-i");
		command.add(videoInputPath);

		command.add("-vcodec");
		command.add("copy");

		command.add("-an");
		command.add(videoOutputPath);


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

		return videoOutputPath;
	}
	//转换
	public void convertor(String videoInputPath,String mp3InputPath,
						  double seconds,String videoOutputPath) throws Exception{
		//ffmpeg.exe -i test.mp4 -i bgm.mp3 -t 6 -y new.mp4
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);

		command.add("-i");
		command.add(videoInputPath);

		command.add("-i");
		command.add(mp3InputPath);

		command.add("-t");
		command.add(String.valueOf(seconds));

		command.add("-y");
		command.add(videoOutputPath);



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
		MergeVideoMp3 ffmpeg = new MergeVideoMp3("C:\\ffmpeg\\bin\\ffmpeg.exe");
		try {
			String videoOutputPath = ffmpeg.copyMp4("E:\\test.mp4","E:\\test01.mp4");
			ffmpeg.convertor(videoOutputPath,
					"E:\\bgm.mp3", 8.0,"E:\\new.mp4");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
