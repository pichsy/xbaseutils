package com.pichs.common.utils.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.SENSOR_SERVICE;
/*******************************************************
 *
 *                            _ooOoo_
 *                           o8888888o
 *                           88" . "88
 *                           (| -_- |)
 *                           O\  =  /O
 *                        ____/`---'\____
 *                      .'  \\|     |//  `.
 *                     /  \\|||  :  |||//  \
 *                    /  _||||| -:- |||||-  \
 *                    |   | \\\  -  /// |   |
 *                    | \_|  ''\---/''  |   |
 *                    \  .-\__  `-`  ___/-. /
 *                  ___`. .'  /--.--\  `. . __
 *               ."" '<  `.___\_<|>_/___.'  >'"".
 *              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *              \  \ `-.   \_ __\ /__ _/   .-` /  /
 *         ======`-.____`-.___\_____/___.-`____.-'======
 *                            `=---='
 *         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 * 虚拟机判断工具类
 * @date 2020/6/17
 * @author pichs
 **********************************************************/
public class VirtualUtils {

    public static boolean isNotPhone(Context context) {
        return notHasBlueTooth()
                || notHasLightSensorManager(context)
                || isFeatures()
                || checkIsNotRealPhone()
                || checkPipes();
    }

    /**
     * 用途:判断蓝牙是否有效来判断是否为模拟器
     * 返回:true 为模拟器
     * 需要蓝牙权限
     */
    private static boolean notHasBlueTooth() {
        try {
            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
            if (ba == null) {
                return true;
            } else {
                // 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器
                String name = ba.getName();
                return TextUtils.isEmpty(name);
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 用途:依据是否存在光传感器来判断是否为模拟器
     * 返回:true 为模拟器
     * 需要传感器权限
     */
    private static Boolean notHasLightSensorManager(Context context) {
        try {
            SensorManager sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
            return null == sensor8;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 用途:根据部分特征参数设备信息来判断是否为模拟器
     * 返回:true 为模拟器
     */
    private static boolean isFeatures() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    /**
     * 用途:根据CPU是否为电脑来判断是否为模拟器
     * 返回:true 为模拟器
     */
    private static boolean checkIsNotRealPhone() {
        String cpuInfo = readCpuInfo();
        return (cpuInfo.contains("intel") || cpuInfo.contains("amd"));
    }

    /**
     * 用途:根据CPU是否为电脑来判断是否为模拟器(子方法)
     * 返回:String
     */
    private static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine;
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 用途:检测模拟器的特有文件
     * 返回:true 为模拟器
     */
    private static String[] known_pipes = {"/dev/socket/qemud", "/dev/qemu_pipe"};

    private static boolean checkPipes() {
        for (int i = 0; i < known_pipes.length; i++) {
            String pipes = known_pipes[i];
            File qemu_socket = new File(pipes);
            if (qemu_socket.exists()) {
                Log.v("Result:", "Find pipes!");
                return true;
            }
        }
        Log.i("Result:", "Not Find pipes!");
        return false;
    }

}
