package p.mbt._class;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BlueTooth {
	
	public BluetoothAdapter myBluetoothadapter;//手机的蓝牙适配器
	public static boolean isConnectOk = false;//蓝牙连接状态的标志
	public static BluetoothSocket msocket = null;//通信socket
	public static InputStream misIn = null;//输入流
	public static OutputStream mosOut = null;//输出流
	public String otherBluetoothMAC; //对方蓝牙的MAC
	
	private Activity myActivity;
	
	public BlueTooth(BluetoothAdapter bluetoothadapter,Activity activity){
		myBluetoothadapter = bluetoothadapter;
		myActivity = activity;
	}
	
	public boolean ConnectBlueTooth(BluetoothDevice device){
		if(isConnectOk)                  //关闭连接，重新连接
		{
			try{
				msocket.close();
				misIn.close();
				mosOut.close();
			}
			catch(IOException ioexception1){
				msocket = null;
				isConnectOk = false;
				misIn = null;
				mosOut = null;
			}
		}
		try{         //建立连接
			msocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
			Log.i("TAG", "socket success");
			msocket.connect();
			mosOut = msocket.getOutputStream();		
			misIn = msocket.getInputStream();
			Log.i("TAG", "connect success");
			isConnectOk = true;
			Toast.makeText(myActivity, "连接成功！", Toast.LENGTH_LONG).show();
			return true;
	         }
            catch(Exception e) {
            Log.e("TAG", "Couldn't establish Bluetooth connection!");
            isConnectOk = false;
            Toast.makeText(myActivity, "连接失败！", Toast.LENGTH_LONG).show();
            return false;
		}
	}
	public void terminateConnect() {
		if (isConnectOk) {
			try {
				isConnectOk = false;
				msocket.close();
				misIn.close();
				mosOut.close();
				} catch (IOException localIOException) {
				misIn = null;
				mosOut = null;
				msocket = null;
				}
			}
		}
	
	public boolean OpenBlueTooth(){
		if( myBluetoothadapter!= null )//本机有蓝牙设备
		{
			if( !myBluetoothadapter.isEnabled() )  //蓝牙没有打开
			{
				// 打开蓝牙
				//创建一个intent对象，该对象用于启动一个activity，提示用户打开蓝牙
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				myActivity.startActivity(intent);
				//bluetoothadapter.enable();//不询问用户，直接打开蓝牙
				Toast.makeText( myActivity,"本机蓝牙已打开！" , Toast.LENGTH_SHORT).show();
				return true;	
			}
			else
			{
				Toast.makeText( myActivity,"本机蓝牙已打开！" , Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		else    //本机无蓝牙设备
		{ 
			Toast.makeText( myActivity,"本机无蓝牙设备,请检查设备后重试！" , Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	public void CloseBlueTooth(){
		myBluetoothadapter.disable();
		isConnectOk = false;
		Toast.makeText( myActivity,"蓝牙已关闭！" , Toast.LENGTH_SHORT).show();
		//////////////////////////////////////////////////////
	}
	
	//接收数据函数
	public int ReceiveData() {
		if (!isConnectOk)
			return -2;
		try {
			Log.i("RE", "reading");
			return misIn.read();
		} catch (IOException ioexception) {
			terminateConnect();
			Log.i("RE", "read exception");
			return -3;
			//Toast
		}
	}
			
	public int SendData(byte bytebuf[]) {
		int bytelength;
		if (isConnectOk) {
			try {
				mosOut.write(bytebuf);
				bytelength = bytebuf.length;
			} catch (IOException ioexception) {
				terminateConnect();
				bytelength = -3;
			}
		} else
			bytelength = -2;
		return bytelength;
	}

}
