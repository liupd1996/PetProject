package com.example.petproject.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtils {
    public static double getBatteryPercentage(String inputString) {
// 使用正则表达式提取电压值
        String voltageValue = extractVoltageValue(inputString);
        try {
            double voltage = Double.parseDouble(voltageValue);
            double batteryPercentage = -3016.5336 + 1400.8166 * voltage + -156.7507 * voltage * voltage;
            return batteryPercentage;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String extractVoltageValue(String inputString) {
        // 定义正则表达式匹配模式
        Pattern pattern = Pattern.compile("总电压:(\\d+\\.\\d+)V");
        Matcher matcher = pattern.matcher(inputString);

        // 查找匹配的电压值
        if (matcher.find()) {
            // 返回提取的电压值
            return matcher.group(1);
        } else {
            // 没有找到匹配的电压值
            return "未找到电压值";
        }
    }

    public static String extractVTodayKm(String inputString) {
        // 定义正则表达式匹配模式
        Pattern pattern = Pattern.compile("(当日里程:)(\\d+\\.\\d+)(km)");
        Matcher matcher = pattern.matcher(inputString);

        // 查找匹配的电压值
        if (matcher.find()) {
            // 返回提取的电压值
            return matcher.group(2);
        } else {
            // 没有找到匹配的电压值
            return "未找到里程值";
        }
    }

    public static double extractBattery(String inputString) {

        // 使用Pattern类编译正则表达式模式
        Pattern r = Pattern.compile("电量:(\\d+)%?");
        // 创建Matcher对象
        Matcher m = r.matcher(inputString);

        // 查找匹配的电量值部分
        if (m.find()) {
            // 提取匹配的电量值
            return Double.parseDouble(m.group(1));
        } else {
            return 0;
        }
    }

    public static double extractXYZ(String inputString) {
        // 定义正则表达式匹配模式

        Pattern pattern = Pattern.compile("\\[([XYZ])轴](-?\\d+)");
        Matcher matcher = pattern.matcher(inputString);

        double maxXValue = -1;
        while (matcher.find()) {
            String axis = matcher.group(1); // 获取轴名称
            String foundValue = matcher.group(2); // 获取匹配到的数字
            maxXValue = Math.max(maxXValue, Math.abs(Double.parseDouble(foundValue)));
        }
        Log.d("1111", "extractXYZ maxXValue: " + maxXValue);
        return maxXValue;
    }


    public static double extractTemperatures(String inputString) {
        // 定义正则表达式匹配模式
        Pattern pattern = Pattern.compile("温度:(\\d+\\.?\\d*)℃,(-?\\d+\\.?\\d*)℃");
        Matcher matcher = pattern.matcher(inputString);

        // 提取两个温度值
        double tempSkin = -1;
        double tempEnvir = -1;

        if (matcher.find()) {
            tempSkin = Double.parseDouble(matcher.group(1)); // 第一个温度值
            tempEnvir = Double.parseDouble(matcher.group(2)); // 第二个温度值
        }

        // 打印提取的温度值
        Log.d("Temperatures", "Skin Temperature: " + tempSkin);
        Log.d("Temperatures", "Environment Temperature: " + tempEnvir);

        // 进行计算并返回结果
        double tempCore = 0.0336 * tempSkin * tempSkin - 0.546 * tempSkin +
                1.7082 * tempEnvir - 0.0514 * tempEnvir * tempSkin + 17.626;
        Log.d("1111", "extractTemperatures tempCore: " + tempCore);
        return tempCore;
    }
    public static void testRegexMatch(String inputString) {
        // 定义正则表达式匹配模式
        Pattern pattern = Pattern.compile("温度:(\\d+\\.?\\d*)℃,(-?\\d+\\.?\\d*)℃");
        Matcher matcher = pattern.matcher(inputString);

        // 尝试匹配正则表达式
        if (matcher.find()) {
            // 输出匹配到的字符串
            Log.d("RegexMatch", "Matched String: " + matcher.group());
            // 输出提取的温度值
            Log.d("RegexMatch", "Temperature 1: " + matcher.group(1));
            Log.d("RegexMatch", "Temperature 2: " + matcher.group(2));
        } else {
            Log.d("RegexMatch", "No match found.");
        }
    }

}
