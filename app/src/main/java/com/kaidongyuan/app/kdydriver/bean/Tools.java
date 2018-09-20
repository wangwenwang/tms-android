package com.kaidongyuan.app.kdydriver.bean;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.kaidongyuan.app.basemodule.utils.nomalutils.SystemUtil;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.kdydriver.ui.activity.LoginActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;

public class Tools{

	private int logo;

	private String name;

	public int getLogo() {
		return logo;
	}

	public void setLogo(int logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tools(int logo, String name) {
		super();
		this.logo = logo;
		this.name = name;
	}






	/**
	 * 通过ping判断是否可用
	 * @return
	 */
	public static boolean ping() {
		try {
			//服务器ip地址
			String ip = "www.baidu.com";
			Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ip);
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content;
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			int status = p.waitFor();
			if (status == 0) {
				return true;
			}
		}
		catch (IOException e) {}
		catch (InterruptedException e) {}
		return false;
	}

	/**
	 * 版本号比较
	 *
	 * @param server 服务器版本号
	 * @param locati 本地版本号
	 * @return
	 */
	public static int compareVersion(String server, String locati){
		if (server.equals(locati)) {
			return 0;
		}
		String[] version1Array = server.split("\\.");
		String[] version2Array = locati.split("\\.");
		Log.d("HomePageActivity", "version1Array==" + version1Array.length);
		Log.d("HomePageActivity", "version2Array==" + version2Array.length);
		int index = 0;
		// 获取最小长度值
		int minLen = Math.min(version1Array.length, version2Array.length);
		int diff = 0;
		// 循环判断每位的大小
		Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
		while (index < minLen
				&& (diff = Integer.parseInt(version1Array[index])
				- Integer.parseInt(version2Array[index])) == 0) {
			index++;
		}
		if (diff == 0) {
			// 如果位数不一致，比较多余位数
			for (int i = index; i < version1Array.length; i++) {
				if (Integer.parseInt(version1Array[i]) > 0) {
					return 1;
				}
			}

			for (int i = index; i < version2Array.length; i++) {
				if (Integer.parseInt(version2Array[i]) > 0) {
					return -1;
				}
			}
			return 0;
		} else {
			return diff > 0 ? 1 : -1;
		}
	}

	/**
	 * 下载进度条
	 */
	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取到文件的大小
			pd.setMax(conn.getContentLength() / 1000 / 1000);
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), LoginActivity.DestFileName);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				//获取当前下载量
				double progressD = total / 1000 / 1000.0;
				String progressS = doubleToString(progressD);
				pd.setProgress(total / 1000 / 1000);
				pd.setProgressNumberFormat(progressS + "m");
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}

	/**
	 * double转String,保留小数点后一位
	 * @param num
	 * @return
	 */
	public static String doubleToString(double num){
		//使用0.0不足位补0，#.#仅保留有效位
		return new DecimalFormat("0.0").format(num);
	}


	public static String inputStream2String (InputStream in) throws IOException {

		StringBuffer out = new StringBuffer();
		byte[]  b = new byte[4096];
		int n;
		while ((n = in.read(b))!= -1){
			out.append(new String(b,0,n));
		}
		Log.i("String的长度",new Integer(out.length()).toString());
		return  out.toString();
	}

	/**
	 * 获取最新版本信息
	 * @return
	 */
	public static JSONObject getServerVersion() {

		String WarehouseCode = "";
		String UserID = "";
		String params = "{\"ConfigCode\":\"" + "APPINFOR" + "\"}";
		String paramsEncoding = URLEncoder.encode(params);
		String Strurl = "https://scm.cy-scm.com/wms/RFAppInfor.do?WarehouseCode=" + WarehouseCode + "&UserID=" + UserID + "&params=" + paramsEncoding;

		HttpURLConnection conn=null;
		try {

			URL url = new URL(Strurl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("AuthCode","cyscm");
			conn.setRequestProperty("AuthID","");
			conn.setRequestMethod("GET");
			if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){

				Log.d("LM","RFAppInfor请求成功");
				InputStream in=conn.getInputStream();
				String resultStr = Tools.inputStream2String(in);
				resultStr = URLDecoder.decode(resultStr,"UTF-8");

				try {
					JSONObject jsonObj = (JSONObject)(new JSONParser().parse(resultStr));
					Log.i("LM",jsonObj.toJSONString() + "\n" + jsonObj.getClass());
					String status = (String)jsonObj.get("status");
					String Msg = (String)jsonObj.get("Msg");
					if(status.equals("1")){

						if(jsonObj.get("data").getClass().getName().equals(org.json.simple.JSONObject.class.getName())) {

							JSONObject dict = (org.json.simple.JSONObject) jsonObj.get("data");
							return dict;
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				in.close();
			}
			else {
				Log.i("PostGetUtil","get请求失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			conn.disconnect();
		}
		return null;
	}

	/**
	 * 获取版本号名称
	 *
	 * @param context 上下文
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().
					getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	/**
	 * 获取当前时间，格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getCurrDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curDate = df.format(new java.util.Date());
		return curDate;
	}

	/**
	 * 计算时间差
	 *
	 * @return 分钟
	 */
	public static long getTimeExpend(String startTime, String endTime){
		//传入字串类型 2016-06-28 08:30:00
		long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
		long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
		long longExpend = longEnd - longStart;  //获取时间差
		long longMinutes = longExpend / (60 * 1000);   //根据时间差来计算分钟数
		return longMinutes;
	}

	private static long getTimeMillis(String strTime) {
		long returnMillis = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;

		try {
			d = sdf.parse(strTime);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			Toast.makeText(LoginActivity.mContext, e.toString(), Toast.LENGTH_SHORT).show();
		}
		returnMillis = d.getTime();
		return returnMillis;
	}


	/**
	 * 跳转手机地图APP
	 *
	 * @param address 地址
	 * @param mContext  上下文
	 * @param appName   APP名字
	 *
	 * @return
	 */
	public static void ToNavigation(String address, Context mContext, String appName) {
		if (SystemUtil.isInstalled(mContext, "com.autonavi.minimap")) {
			//跳转到高德导航
			Intent autoIntent = new Intent();
			try {
				autoIntent.setData(Uri
						.parse("androidamap://route?" +
								"sourceApplication=" + appName +
								"&slat=" + "" +
								"&slon=" + "" +
								"&dlat=" + "" +
								"&dlon=" + "" +
								"&dname=" + address +
								"&dev=0" +
								"&m=2" +
								"&t=0"
						));
			}catch (Exception e) {
				Log.i("LM", "高德地图异常" + e);
			}
			mContext.startActivity(autoIntent);
		}else if (SystemUtil.isInstalled(LoginActivity.mContext,"com.baidu.BaiduMap")){
			//跳转到百度导航
			try {
				Intent baiduintent = Intent.parseUri("intent://map/direction?" +
						"origin=" + "" +
						"&destination=" + address +
						"&mode=driving" +
						"&src=Name|AppName" +
						"#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
				mContext.startActivity(baiduintent);
			} catch (URISyntaxException e) {
				MLog.d("URISyntaxException : " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			Toast.makeText(mContext, "未检索到本机已安装‘百度地图’或‘高德地图’App", LENGTH_LONG).show();
		}
	}
}
