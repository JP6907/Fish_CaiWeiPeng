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
	
	public BluetoothAdapter myBluetoothadapter;//�ֻ�������������
	public static boolean isConnectOk = false;//��������״̬�ı�־
	public static BluetoothSocket msocket = null;//ͨ��socket
	public static InputStream misIn = null;//������
	public static OutputStream mosOut = null;//�����
	public String otherBluetoothMAC; //�Է�������MAC
	
	private Activity myActivity;
	
	public BlueTooth(BluetoothAdapter bluetoothadapter,Activity activity){
		myBluetoothadapter = bluetoothadapter;
		myActivity = activity;
	}
	
	public boolean ConnectBlueTooth(BluetoothDevice device){
		if(isConnectOk)                  //�ر����ӣ���������
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
		try{         //��������
			msocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
			Log.i("TAG", "socket success");
			msocket.connect();
			mosOut = msocket.getOutputStream();		
			misIn = msocket.getInputStream();
			Log.i("TAG", "connect success");
			isConnectOk = true;
			Toast.makeText(myActivity, "���ӳɹ���", Toast.LENGTH_LONG).show();
			return true;
	         }
            catch(Exception e) {
            Log.e("TAG", "Couldn't establish Bluetooth connection!");
            isConnectOk = false;
            Toast.makeText(myActivity, "����ʧ�ܣ�", Toast.LENGTH_LONG).show();
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
		if( myBluetoothadapter!= null )//�����������豸
		{
			if( !myBluetoothadapter.isEnabled() )  //����û�д�
			{
				// ������
				//����һ��intent���󣬸ö�����������һ��activity����ʾ�û�������
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				myActivity.startActivity(intent);
				//bluetoothadapter.enable();//��ѯ���û���ֱ�Ӵ�����
				Toast.makeText( myActivity,"���������Ѵ򿪣�" , Toast.LENGTH_SHORT).show();
				return true;	
			}
			else
			{
				Toast.makeText( myActivity,"���������Ѵ򿪣�" , Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		else    //�����������豸
		{ 
			Toast.makeText( myActivity,"�����������豸,�����豸�����ԣ�" , Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	public void CloseBlueTooth(){
		myBluetoothadapter.disable();
		isConnectOk = false;
		Toast.makeText( myActivity,"�����ѹرգ�" , Toast.LENGTH_SHORT).show();
		//////////////////////////////////////////////////////
	}
	
	//�������ݺ���
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
