package cc.eguid.FFmpegCommandManager.util;

import cc.eguid.FFmpegCommandManager.config.FFmpegConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static cc.eguid.FFmpegCommandManager.util.PropertiesUtil.load;

public class FormatAnalysis {
    /**
     * 获取源文件音视频格式
     *
     * @param input 源文件路径
     */
    public static FFmpegConfig config = (FFmpegConfig) load("loadFFmpeg.properties", FFmpegConfig.class);

    public static String getVideoFormat(String input) {

        return FFprobeCommond(input, "v:0", null);
    }

    public static String getAudioFormat(String input) {
        return FFprobeCommond(input, "a:0", null);
    }

    public static boolean IsH264(String input) {
        if (FFprobeCommond(input, "v:0", null).toLowerCase() == "h264") {
            return true;
        }
        return false;
    }

    public static boolean IsAAC(String input) {
        if (FFprobeCommond(input, "a:0", null).toLowerCase() == "aac") {
            return true;
        }
        return false;
    }

    /**
     * 获取源文件音视频格式
     *
     * @input 需要探测的文件（rtmp/rtsp file）
     * @streamIdex a:0 音频第一个流 v:0 视频第一个流
     * @filter 获取内容过滤条件 null 表示默认获取codec_name
     */
    private static String FFprobeCommond(String input, String streamIdex, String filter) {

        String line = null;
        StringBuilder comm = new StringBuilder(config.getPath() + " -show_streams -select_streams ");
        filter = (filter == null ? "codec_name" : filter);

        comm.append(streamIdex);
        comm.append(input);


        StringBuilder sb = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(comm.toString());
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(process.getInputStream()));


            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
                if (line.contains(filter)) {
                    System.out.println(line);
                    return line;
                }
            }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

        return null;
    }
}
