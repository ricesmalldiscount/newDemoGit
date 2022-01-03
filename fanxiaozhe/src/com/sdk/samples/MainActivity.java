package com.sdk.samples;

import com.apicloud.superwebview.demo.R;
import com.sdk.samples.apicloud.BlockMasking;
import com.sdk.samples.apicloud.WebPageModule;
import com.uzmap.pkg.uzkit.request.APICloudHttpClient;
import com.uzmap.pkg.uzkit.request.HttpGet;
import com.uzmap.pkg.uzkit.request.HttpResult;
import com.uzmap.pkg.uzkit.request.RequestCallback;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

/**
 * 
 * 原生主界面Activity
 * @author dexing.li
 *
 */
public class MainActivity extends Activity implements View.OnClickListener{

	BlockMasking mBlockMasking;
	
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener();
    //初始化定位
    mLocationClient = new AMapLocationClient(getApplicationContext());
    //设置定位回调监听
    mLocationClient.setLocationListener(mLocationListener);
	//声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //初始化AMapLocationClientOption对象
    mLocationOption = new AMapLocationClientOption();

	AMapLocationClientOption option = new AMapLocationClientOption();
    /**
     * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
     */
    option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
    if(null != locationClient){
      locationClient.setLocationOption(option);
      //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
      locationClient.stopLocation();
      locationClient.startLocation();
    }

	//设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
    mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
	//设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
    //mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
	//设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
    //mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

	
    //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
    //mLocationOption.setInterval(1000);

	//设置是否返回地址信息（默认返回地址信息）
    //mLocationOption.setNeedAddress(true);

	//设置是否允许模拟位置,默认为true，允许模拟位置
    //mLocationOption.setMockEnable(true);

	//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
    //mLocationOption.setHttpTimeOut(20000);

	//关闭缓存机制
    //mLocationOption.setLocationCacheEnable(false);

	//给定位客户端对象设置定位参数
    mLocationClient.setLocationOption(mLocationOption);
    //启动定位
    mLocationClient.startLocation();
	
	//可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    //以下为后者的举例：
    AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
    if (amapLocation != null) {
    if (amapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
    }else {
    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
    Log.e("AmapError","location Error, ErrCode:"
        + amapLocation.getErrorCode() + ", errInfo:"
        + amapLocation.getErrorInfo());
    }
}
    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
    amapLocation.getLatitude();//获取纬度
    amapLocation.getLongitude();//获取经度
    amapLocation.getAccuracy();//获取精度信息
    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    amapLocation.getCountry();//国家信息
    amapLocation.getProvince();//省信息
    amapLocation.getCity();//城市信息
    amapLocation.getDistrict();//城区信息
    amapLocation.getStreet();//街道信息
    amapLocation.getStreetNum();//街道门牌号信息
    amapLocation.getCityCode();//城市编码
    amapLocation.getAdCode();//地区编码
    amapLocation.getAoiName();//获取当前定位点的AOI信息
    amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
    amapLocation.getFloor();//获取当前室内定位的楼层
    amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
    //获取定位时间
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date(amapLocation.getTime());
    df.format(date);
  }
}
    
	mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁

	mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindListenner();
    }

    protected void bindListenner() {
        findViewById(R.id.item1).setOnClickListener(this);
        findViewById(R.id.item2).setOnClickListener(this);
        findViewById(R.id.item3).setOnClickListener(this);
        findViewById(R.id.item4).setOnClickListener(this);
        findViewById(R.id.SuperWebview).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.SuperWebview){
			Intent intent = new Intent(this, WebPageModule.class);
			//不传递startUrl的情况下，默认走自动加载widget的机制，即：APICloud引擎会自动去解析assets/widget目录下的资源并加载
//	        String url = "file://" + UZUtility.getExternaStoragePath() + "index.html"; 
//			intent.putExtra("startUrl", "file:///android_asset/widget/index.html");
//	        intent.putExtra("startUrl", url);
	        startActivity(intent);
		}else if(id == R.id.item4){
			httpClientTest();
		}else{
			Toast.makeText(this, "即将开启", Toast.LENGTH_SHORT).show();
		}
	}
    
	/**
	 *APICloud网络请求框架测试Case
	 */
	private void httpClientTest(){
		mBlockMasking = new BlockMasking(this);
		mBlockMasking.setMessage("正在发送请求....");
		mBlockMasking.show();
		//Get请求
		HttpGet request = new HttpGet("http://www.baidu.com");
		//超时时间10秒
		request.setTimeout(10);
		//设置UA，不设置则取APICloud模块UA
		request.addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.3.4; en-us; Nexus S Build/GRJ22) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		//请求结果监听
		request.setCallback(new RequestCallback() {
			@Override
			public void onFinish(final HttpResult result) {
				//在主线程中操作UI，网络请求是非主线程回调的
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mBlockMasking.dismiss();
						showAlert("请求结果：\n" + result.data);
					}
				});
			}
		});
		APICloudHttpClient.instance().request(request);
	}
	
	private void showAlert(String message){
		AlertDialog.Builder dia = new AlertDialog.Builder(this);
		dia.setTitle("提示消息");
		dia.setMessage(message);
		dia.setCancelable(false);
		dia.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				;
			}
		});
		dia.show();
	}
	
	@Override
	public void finish() {
		super.finish();
		Process.killProcess(Process.myPid());
	}

}
