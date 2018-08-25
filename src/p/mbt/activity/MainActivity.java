package p.mbt.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import p.mbt.R;
import p.mbt._class.BlueTooth;
import p.mbt._class.DrawChart;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener, OnCheckedChangeListener,  OnItemSelectedListener {

	// 请求打开蓝牙
	private static final int REQUEST_ENABLE = 0X1;
	// 请求能够被发现
	private static final int REQUEST_DISCOVERABLE = 0X2;
	
	private final static int BlueToothRequestCode = 1;
	private final static int LoseBlueToothConnect = 2;
	
	private final static int ReceiveHead = 0xef;  //接收数据起始码
	private final static int ReceiveTail = 0xfe;  //接收数据结束码
	
	private List<Integer> receiveCache = new ArrayList<Integer>();  //接收数据缓存
	private List<Integer> receiveShowCache = new ArrayList<Integer>();  //接收数据缓存
	
	private static int MessageGetData = 11; // 消息队列
	private static int MessageReceive = 12;

	
//	public static final String[] modifyFeed = {"1次","2次","3次"};
//	public static final String[] modifyOx = {"20%","40%","60%"};
	
	private boolean isReceiving = false;
	
	
	private boolean isShowLine1 = true;
	private boolean isShowLine2 = true;

	TextView tv_device;  //连接的设备
	TextView tvData;  //原始数据 
//	TextView tvDataTem;  //温度  
//	TextView tvDataHum;  //湿度
//	TextView tvDataLine;  //供氧量
	CheckBox cbTem;  //温度
	CheckBox cbHum;  //湿度
	CheckBox cbReceiveData;

	BlueTooth bluetooth;
	Vibrator vibrator;  //声明一个振动器对象
	
	RadioButton rbUpTem;   //升温
	RadioButton rbHoldTem; //保持温度温
	RadioButton rbDownTem; //降温
	RadioButton rbAddFeed; //添加饲料
	RadioButton rbNoFeed;  //不添加饲料
	RadioButton rbHighLine; //强光
	RadioButton rbLightLine; //弱光
//	RadioButton rbAddOx;    //增加供氧量
//	RadioButton rbNoOx;   //不增加供氧量
	
	RadioGroup rgTemperate;
	RadioGroup rgFeed;
	RadioGroup rgLine;
//	RadioGroup rgOx;
	
	Spinner spinnerHighTem;
	Spinner spinnerLowTem;
	Spinner spinnerHighHum;
	Spinner spinnerLowHum;
	
	Button btSetData;  //设置状态
	Button btAddFeed;  //添加饲料

	private DrawChart view;

	private int layoutTop;
	private int layoutWidth;
	private int layoutHeigth;


	boolean isDrawDeepthLine = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 去标题全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		UIInit();

		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		bluetooth = new BlueTooth(BluetoothAdapter.getDefaultAdapter(), this);
		
	}

	private void UIInit() {
		tv_device = (TextView) this.findViewById(R.id.textview1);
//		tvDataTem = (TextView) this.findViewById(R.id.tv_dataTem);
//		tvDataHum = (TextView) this.findViewById(R.id.tv_dataHum);
//		tvDataLine = (TextView) this.findViewById(R.id.tv_dataLine);
		tvData = (TextView) this.findViewById(R.id.tv_data);
		
		
		rgTemperate = (RadioGroup) this.findViewById(R.id.rg_temperate);
		rgFeed = (RadioGroup) this.findViewById(R.id.rg_feed);
		rgLine = (RadioGroup) this.findViewById(R.id.rg_line);
//		rgOx = (RadioGroup) this.findViewById(R.id.rg_ox);

		rbUpTem = (RadioButton) this.findViewById(R.id.rb_up_temperate);
		rbHoldTem = (RadioButton) this.findViewById(R.id.rb_hold_temperate);
		rbDownTem = (RadioButton) this.findViewById(R.id.rb_down_temperate);
		rbAddFeed = (RadioButton) this.findViewById(R.id.rb_add_feed);
		rbNoFeed = (RadioButton) this.findViewById(R.id.rb_no_feed);
		rbHighLine = (RadioButton) this.findViewById(R.id.rb_high_line);
		rbLightLine = (RadioButton) this.findViewById(R.id.rb_light_line);
//		rbAddOx = (RadioButton) this.findViewById(R.id.rb_add_ox);
//		rbNoOx = (RadioButton) this.findViewById(R.id.rb_no_ox);
		
		btSetData = (Button) this.findViewById(R.id.setData);
		btSetData.setOnClickListener(this);
		btSetData.getBackground().setAlpha(100);
		
		btAddFeed = (Button) this.findViewById(R.id.addFeed);
		btAddFeed.setOnClickListener(this);
		btAddFeed.getBackground().setAlpha(100);

        cbTem = (CheckBox) this.findViewById(R.id.checkBox1);
        cbTem.setOnCheckedChangeListener(this);
        cbHum = (CheckBox) this.findViewById(R.id.checkBox2);
        cbHum.setOnCheckedChangeListener(this);

		cbReceiveData = (CheckBox) this.findViewById(R.id.receiveData);
		cbReceiveData.setOnCheckedChangeListener(this);
		cbReceiveData.getBackground().setAlpha(100);
		
		spinnerHighTem = (Spinner)this.findViewById(R.id.spin_highTem);
		spinnerHighTem.setOnItemSelectedListener(this);
		spinnerLowTem = (Spinner)this.findViewById(R.id.spin_lowTem);
		spinnerLowTem.setOnItemSelectedListener(this);
		spinnerHighHum = (Spinner)this.findViewById(R.id.spin_highHum);
		spinnerHighHum.setOnItemSelectedListener(this);
		spinnerLowHum = (Spinner)this.findViewById(R.id.spin_lowHum);
		spinnerLowHum.setOnItemSelectedListener(this);

		LinearLayout layout = (LinearLayout) findViewById(R.id.root);
		view = new DrawChart(this);

		layoutTop = layout.getTop();
		layoutWidth = layout.getWidth();
		layoutHeigth = layout.getHeight();

		view.getLayoutData(layoutTop, layoutWidth, layoutHeigth);

		view.invalidate(); // 窗口无效，窗口重绘
		for(int i=0;i<10;i++){
			view.getData1(1);
			view.getData2(1);
		}
		layout.addView(view);
		view.invalidate(); // 窗口无效，窗口重绘
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case BlueToothRequestCode:
			final BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			Log.i("data", "Get device");
			if (bluetooth.ConnectBlueTooth(device)) {
				Log.i("data", "连接成功");
				tv_device.setText("已连接到设备:" + device.getName());
				//receiveDataThread.start();
				new ReceiveDataThread(bluetooth).start();
			}
			break;
		}
	}
	/*
	 * 数据接收线程
	 */
	private class ReceiveDataThread extends Thread {
		private BlueTooth blueTooth;
		private int receivedVale;

		public ReceiveDataThread(BlueTooth blueTooth) {
			this.blueTooth = blueTooth;
		}

		@Override
		public void run() {
			while (bluetooth.isConnectOk) {
				Log.i("data", "Receive data ing");
				do {
					if (isReceiving) {
						receivedVale = bluetooth.ReceiveData();
						if (receivedVale < 0) {
							Log.e("data", "receiveVale < 0, read fail");
							break;
						} else {
								Log.i("receiveData", receivedVale + "");
//								if(receiveShowCache.size()<5){
//									receiveShowCache.add(receivedVale);
//								}else{
//									receiveShowCache.add(receivedVale);
//									Bundle bundle = new Bundle();
//									//bundle.putInt("data", receivedVale);
//									bundle.putIntArray("data", ListToIntArray(receiveShowCache));
//									Message msg1 = new Message();
//									msg1.setData(bundle);
//									msg1.what = MessageReceive;
//									handler.sendMessage(msg1);
//									receiveShowCache.clear();
//								}
								if(receiveCache.size()==10){
									receiveCache.clear();
								}
								if(receivedVale == ReceiveHead){  //协议头
									receiveCache.clear();
									receiveCache.add(receivedVale);
								}else if(receivedVale == ReceiveTail){ //协议尾
//									Bundle bundle = new Bundle();
//									bundle.putInt("data", receivedVale);
									receiveCache.add(receivedVale);
									Message msg = new Message();
//									msg.setData(bundle);
									msg.what = MessageGetData;
									handler.sendMessage(msg);
								}else{
									receiveCache.add(receivedVale);
								} 
								
						}
					}
				} while (true);
				Log.w("data", "break");
			}
			Message msg1 = new Message();
			msg1.what = LoseBlueToothConnect;
			handler.sendMessage(msg1);
		}
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			if (msg.what == MessageGetData) {
					//int data = msg.getData().getInt("data");
//					if(tvData.getText().length()<30)
//						tvData.append(data+"");
//					else
//						tvData.setText(data+"");
//					tvData.setText("原始数据："+Integer.toHexString(receiveCache.get(0))+" ");
//					for(int i=1;i<receiveCache.size();i++){
//						tvData.append(Integer.toHexString(receiveCache.get(i))+" ");
//					}
					if(receiveCache.size()==5
						&&receiveCache.get(0)==ReceiveHead&&receiveCache.get(4)==ReceiveTail){
						tvData.setTextColor(Color.BLACK);
						tvData.setText("毕业设计（蔡伟鹏）  ~~~");
						int temperate = receiveCache.get(1); //温度
						int humidity = receiveCache.get(2);
						int line = receiveCache.get(3);
//						tvDataTem.setText(" "+temperate);
//						tvDataHum.setText(" "+humidity);
//						if(line==0x10)
//							tvDataLine.setText("强光");
//						else if(line==0x11)
//							tvDataLine.setText("弱光");
//						else
//							tvDataLine.setText("数据错误："+line);
						view.getData1(temperate);
						view.getData2(humidity);
						view.invalidate();
					}else if(receiveCache.size()==4
							&&receiveCache.get(0)==ReceiveHead&&receiveCache.get(3)==ReceiveTail){
						if(receiveCache.get(1)==0xff&&receiveCache.get(2)==0xee){//温度过高
							/**
					         * 四个参数就是——停止 开启 停止 开启
					         * -1不重复，非-1为从pattern的指定下标开始重复
					         */
							tvData.setTextColor(Color.RED);
							tvData.setText("警告！警告！警告！温度过高！！！");
							vibrator.vibrate(new long[]{1000, 5000, 1000, 5000}, -1);
							Toast.makeText(MainActivity.this, "温度过高！！！", Toast.LENGTH_LONG).show();
						}else if(receiveCache.get(1)==0x11&&receiveCache.get(2)==0x22){ //温度过低
							/**
					         * 四个参数就是——停止 开启 停止 开启
					         * -1不重复，非-1为从pattern的指定下标开始重复
					         */
							tvData.setTextColor(Color.RED);
							tvData.setText("警告！警告！警告！温度过低！！！");
							vibrator.vibrate(new long[]{1000, 5000, 1000, 5000}, -1);
							Toast.makeText(MainActivity.this, "温度过低！！！", Toast.LENGTH_LONG).show();
						}
					}else if(receiveCache.size()==3
							&&receiveCache.get(0)==ReceiveHead&&receiveCache.get(2)==ReceiveTail){
						if(receiveCache.get(1)==0xab){
							tvData.setTextColor(Color.RED);
							tvData.setText("请添加饲料！！！");
						}
					}else{
//						tvDataTem.setText("当前数据出错！  "+receiveCache.size());
//						tvDataHum.setText("当前数据出错！  "+Integer.toHexString((receiveCache.get(0))));
//						tvDataLine.setText("当前数据出错！ "+Integer.toHexString((receiveCache.get(receiveCache.size()-1))));
					}
				}  else if (msg.what == LoseBlueToothConnect) {
					Toast.makeText(MainActivity.this, "连接中断！", Toast.LENGTH_LONG).show();
				} else if(msg.what == MessageReceive){
					int[] data = msg.getData().getIntArray("data");
					for(int i:data)
						tvData.append(Integer.toHexString(i)+" ");
				}
	
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.lookData:	
				Intent intent = new Intent(this, DataActivity.class);
				startActivity(intent);
			break;*/
		case R.id.addFeed:
			byte[] data1 = intToByteArray(0xaa);
			bluetooth.SendData(data1);
			break;
		case R.id.setData:
			//rgTemperate.getCheckedRadioButtonId()
			if(rbHoldTem.isChecked()&&rbNoFeed.isChecked()&&rbLightLine.isChecked()){
				byte[] data = intToByteArray(0x00);
				bluetooth.SendData(data);
			}else if(rbUpTem.isChecked()&&rbNoFeed.isChecked()&&rbLightLine.isChecked()){
				byte[] data = intToByteArray(0x08);
				bluetooth.SendData(data);
			}else if(rbDownTem.isChecked()&&rbNoFeed.isChecked()&&rbLightLine.isChecked()){
				byte[] data = intToByteArray(0x04);
				bluetooth.SendData(data);
			}else if(rbHoldTem.isChecked()&&rbAddFeed.isChecked()&&rbLightLine.isChecked()){
				byte[] data = intToByteArray(0x02);
				bluetooth.SendData(data);				
			}else if(rbHoldTem.isChecked()&&rbNoFeed.isChecked()&&rbHighLine.isChecked()){
				byte[] data = intToByteArray(0x01);
				bluetooth.SendData(data);
			}else if(rbUpTem.isChecked()&&rbAddFeed.isChecked()&&rbLightLine.isChecked()){
				byte[] data = intToByteArray(0x0a);
				bluetooth.SendData(data);
			}else if(rbDownTem.isChecked()&&rbAddFeed.isChecked()&&rbLightLine.isChecked()){
				byte[] data = intToByteArray(0x06);
				bluetooth.SendData(data);
			}else if(rbUpTem.isChecked()&&rbAddFeed.isChecked()&&rbHighLine.isChecked()){
				byte[] data = intToByteArray(0x0b);
				bluetooth.SendData(data);
			}else if(rbDownTem.isChecked()&&rbAddFeed.isChecked()&&rbHighLine.isChecked()){
				byte[] data = intToByteArray(0x07);
				bluetooth.SendData(data);
			}else if(rbHoldTem.isChecked()&&rbAddFeed.isChecked()&&rbHighLine.isChecked()){
				byte[] data = intToByteArray(0x03);
				bluetooth.SendData(data);
			}else if(rbUpTem.isChecked()&&rbNoFeed.isChecked()&&rbHighLine.isChecked()){
				byte[] data = intToByteArray(0x05);
				bluetooth.SendData(data);
			}
			else if(rbDownTem.isChecked()&&rbNoFeed.isChecked()&&rbHighLine.isChecked()){
				byte[] data = intToByteArray(0x09);
				bluetooth.SendData(data);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.button_search: {
			// Intent intent= new Intent();
			// intent.setClass(MainActivity.this, Blueactivity.class);
			// startActivity(intent);
			Intent enabler2 = new Intent(this, BlueSearchActivity.class);
			startActivityForResult(enabler2, 10);
		}
			break;
		case R.id.button_open: {
			bluetooth.OpenBlueTooth();
		}
			break;
		case R.id.button_close: {
			bluetooth.CloseBlueTooth();
			tv_device.setText("device--------");
		}
			break;
		case R.id.button_see: {
			Intent enablersee = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			enablersee.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivityForResult(enablersee, REQUEST_DISCOVERABLE);
		}
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == cbTem) {
			if(isChecked){
				isShowLine1 = true;
				view.setDraw(isShowLine1, isShowLine2);
			} else if (!isChecked){
				isShowLine1 = false;
				view.setDraw(isShowLine1, isShowLine2);
			}
		} else if (buttonView == cbHum) {
			if(isChecked){
				isShowLine2 = true;
				view.setDraw(isShowLine1, isShowLine2);
			} else if (!isChecked){
				isShowLine2 = false;
				view.setDraw(isShowLine1, isShowLine2);
			}
		} else if (buttonView == cbReceiveData) {
			Log.i("cb", "in");
			if (isChecked) {
				isReceiving = true;
				cbReceiveData.setText("     暂停接收");
				Log.i("cb", "ischecked");
				//bluetooth.SendData("a".getBytes());
				//dataManager.createNewFile();
			} else if (!isChecked) {
				isReceiving = false;
				cbReceiveData.setText("     开始接收");
				Log.i("cb", "isnotcheck");
				//bluetooth.SendData("b".getBytes());
			}
		} 

	}
	public static byte[] intToByteArray(int a) {   
		return new byte[] {   
//		        (byte) ((a >> 16) & 0xFF),   
//		        (byte) ((a >> 8) & 0xFF),      
//		        (byte) (a & 0xFF)
		        (byte) (a & 0xFF)   
		    };   
	}
	public static void wait(int mills){
		try {
				Thread.currentThread().sleep(mills*1000); //毫秒
			} catch (Exception e) {
			 e.printStackTrace();
			}
	}
	public static int[] ListToIntArray(List<Integer> list){
		int[] array = new int[list.size()];
		for(int i=0;i<list.size();i++){
			array[i] = list.get(i);
		}
		return array;
	}
	//从缓存数据获取数据
//	public void showData(int[] reveiveData){
//		
//		return false;
//	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		/*
         * parent接收的是被选择的数据项所属的 Spinner对象，
         * view参数接收的是显示被选择的数据项的TextView对象
         * position接收的是被选择的数据项在适配器中的位置
         * id被选择的数据项的行号
         */
		switch(parent.getId()){
			case R.id.spin_highTem:
				if(spinnerHighTem.getSelectedItem().equals("30℃")){
					byte[] data = intToByteArray(0xa0);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置高温阈值为30℃", Toast.LENGTH_SHORT).show();
				}else if(spinnerHighTem.getSelectedItem().equals("35℃")){
					byte[] data = intToByteArray(0xa1);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置高温阈值为35℃", Toast.LENGTH_SHORT).show();
				}else if(spinnerHighTem.getSelectedItem().equals("40℃")){
					byte[] data = intToByteArray(0xa2);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置高温阈值为40℃", Toast.LENGTH_SHORT).show();
				}
			break;
			case R.id.spin_lowTem:
				if(spinnerLowTem.getSelectedItem().equals("20℃")){
					byte[] data = intToByteArray(0xa3);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置低温阈值为20℃", Toast.LENGTH_SHORT).show();
				}else if(spinnerLowTem.getSelectedItem().equals("15℃")){
					byte[] data = intToByteArray(0xa4);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置低温阈值为15℃", Toast.LENGTH_SHORT).show();
				}else if(spinnerLowTem.getSelectedItem().equals("10℃")){
					byte[] data = intToByteArray(0xa5);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置低温阈值为10℃", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.spin_highHum:
				if(spinnerHighHum.getSelectedItem().equals("80%")){
					byte[] data = intToByteArray(0xa6);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置高湿度阈值为80%", Toast.LENGTH_SHORT).show();
				}else if(spinnerHighHum.getSelectedItem().equals("85%")){
					byte[] data = intToByteArray(0xa7);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置高湿度阈值为85%", Toast.LENGTH_SHORT).show();
				}else if(spinnerHighHum.getSelectedItem().equals("90%")){
					byte[] data = intToByteArray(0xa8);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置高湿度阈值为90%", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.spin_lowHum:
				if(spinnerLowHum.getSelectedItem().equals("30%")){
					byte[] data = intToByteArray(0xb0);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置低湿度阈值为30%", Toast.LENGTH_SHORT).show();
				}else if(spinnerLowHum.getSelectedItem().equals("25%")){
					byte[] data = intToByteArray(0xb1);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置低湿度阈值为25%", Toast.LENGTH_SHORT).show();
				}else if(spinnerLowHum.getSelectedItem().equals("20%")){
					byte[] data = intToByteArray(0xb2);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置湿度阈值为20%", Toast.LENGTH_SHORT).show();
				}else if(spinnerLowHum.getSelectedItem().equals("85%")){
					byte[] data = intToByteArray(0xb3);
					bluetooth.SendData(data);
					Toast.makeText(this, "设置湿度阈值为85%", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
