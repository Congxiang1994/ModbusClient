package com.congxiang.modbus.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.congxiang.modbus.util.ByteUtil;

public class ControlFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	Socket server;
	InputStream inPutStream;
	OutputStream outPutStream;
	BufferedInputStream buffInputStream;
	BufferedOutputStream buffOutputStream;
	boolean connectionStarted; // 标识量，当程序终止的时候，置为false，可以终止主线程
	
	// 设置服务器地址
	JLabel laIPAddress=new JLabel("IP:", SwingConstants.RIGHT);
	JLabel laPort=new JLabel("Port:", SwingConstants.RIGHT);
	JTextArea taIPAddress = new JTextArea("127.0.0.1"); //　IP地址
	JTextArea taPort = new JTextArea("8000"); //　端口
	JButton btConnectServer=new JButton("连接服务器"); // 连接按钮
	
	// 设置modbus消息，用于查询下位设备是否上线
	JLabel laModbusMsg = new JLabel("modbus命令：", SwingConstants.RIGHT);
	JTextArea taModbusMsg = new JTextArea("01030000000AC5CD");
	JScrollPane jsp = new JScrollPane(taModbusMsg); //　滚动栏
	JButton btUpdateModbusMsg = new JButton("更新modbus命令");
	
	// 从服务器获取modbus终端和下位设备的状态
	JLabel laStateData =new JLabel("状态数据:", SwingConstants.RIGHT);
	JTextArea taStateData = new JTextArea();
	JScrollPane jspStateData =new JScrollPane(taStateData); // 滚动栏
	JButton btGetStateData = new JButton("获取状态");
	
	// 树形图面板
	//TreePanel treePanel = new TreePanel();
	
	ControlFrame() {
		this.setTitle("上位机程序");
		this.setSize(800, 600); // 设置界面大小
		
		//设置控件位置
		Container mainFrame=this.getContentPane();
		mainFrame.setLayout(null);
		mainFrame.add(laIPAddress);
		mainFrame.add(laPort);
		mainFrame.add(taIPAddress);
		mainFrame.add(taPort);
		mainFrame.add(btConnectServer);
		
		mainFrame.add(laModbusMsg);
		mainFrame.add(jsp);
		mainFrame.add(btUpdateModbusMsg);
		
		mainFrame.add(laStateData);
		mainFrame.add(jspStateData);
		mainFrame.add(btGetStateData);
		
/*		mainFrame.add(treePanel);
		treePanel.setBounds(0, 0, 500, 400);
		treePanel.btDevice00.setText("111");*/
		
		laIPAddress.setBounds(5,5,30,20);
		laPort.setBounds(5,25,30,20);
		taIPAddress.setBounds(45, 5, 100, 19);
		taPort.setBounds(45, 25, 100, 19);
		btConnectServer.setBounds(5, 45, 140, 20);
		btConnectServer.addActionListener(this); // 连接服务器
		
		laModbusMsg.setBounds(5,70,100,20);
		jsp.setBounds(105, 70, 140, 50);
		btUpdateModbusMsg.setBounds(5, 120, 240, 20);
		btUpdateModbusMsg.addActionListener(this);  // 更新modbus消息
		
		laStateData.setBounds(5, 145, 100, 20);
		jspStateData.setBounds(105, 145, 140, 50);
		btGetStateData.setBounds(5, 200, 240, 20); 
		btGetStateData.addActionListener(this); // 从后端数据库获取数据
		
		this.setLocationRelativeTo(null); // 设置界面在屏幕中央显示
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置单击关闭按钮能够关闭主进程
		this.setVisible(true); // 显示窗口

		new Thread(new MainThread()).start(); // 启动主线程
		
	}

	class MainThread implements Runnable{

		public MainThread() {
			super();
		}
		
		@Override
		public void run() {
			
		}
	}
	
	/**
	 * 定时从server服务器程序取数据
	 * */
	class MonitorData implements Runnable{
		Boolean monitorDataStarted = true;
		
		public MonitorData() {
			super();
		}

		@Override
		public void run() {
			while (monitorDataStarted) {
				System.out.println("上位机：开始从server取数据");
				
			}
			
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btConnectServer) {
			/** 请求连接服务器 */
			System.out.println("上位机：开始连接服务器...");
			try {
				server = new Socket(taIPAddress.getText().trim(), Integer.valueOf(taPort.getText().trim()));
				inPutStream = server.getInputStream();
				outPutStream = server.getOutputStream();

				buffInputStream = new BufferedInputStream(server.getInputStream());
				buffOutputStream = new BufferedOutputStream(server.getOutputStream());

				System.out.println("上位机：获取输入输出流成功！");
				
				Thread.sleep(1000);// 睡眠一秒，等待server服务器建立相关的接收数据，否则服务器端没法正确接收数据
				
				byte[] buffID = new byte[] { 0x00 };
				buffOutputStream.write(buffID); // 发送一个0x00，表示自己是上位机//---------------------------------------------------------------------write
				buffOutputStream.flush();
				
				Thread.sleep(500);
				
				buffInputStream.read(buffID);// ---------------------------------------------------------------------read
				
				System.out.println("上位机：成功接收server服务器端返回的上线消息 0x00");

			} catch (IOException e1) {
				try {
					server.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.err.println("连接服务器程序出错，请先打开服务器程序！！！");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == btUpdateModbusMsg) {
			System.out.println("上位机：开始更新数据库中的mdobus消息...");
			try {
				// 将文本框中的消息先进行拆分
				String strModbusMsg = taModbusMsg.getText().trim();
				String[] strModbus = strModbusMsg.split("\n"); // 根据回车将消息拆分

				// 将string数组转换成一个个byte数组，然后通过一个循环，将数据发送给server服务器程序
				String strMsg = ""; // 消息类型
				byte[] buffSend;
				for (int i = 0; i < strModbus.length; i++) {
					strMsg = "06";
					strMsg = strMsg + strModbus[i];
					buffSend = ByteUtil.hexStringToBytes(strMsg);

					// 发送消息给server服务器
					buffOutputStream.write(buffSend);// ---------------------------------------------------------------------write
					buffOutputStream.flush();
					buffInputStream.read(buffSend);// ---------------------------------------------------------------------read
					
				}
			} catch (IOException e1) {
				System.err.println("发送modbus命令给server出错，请检查以太网连接！！！");
			}
		} else if (e.getSource() == btGetStateData) {
			System.out.println("上位机：开始想服务器请求设备状态数据..."); // 这里一次性将数据统统取出来
			try {
			//消息类型为0x08 ，这里只需要发送一个消息类型就行，不需要附带其他数据
			String strMsg = "08";
			byte[] buffSend;
			byte[] buffRecv = new byte[10000]; // 用来接收server服务器返回的设备状态数据，暂时长度设定为10000，支持不超过180个设备
			buffSend = ByteUtil.hexStringToBytes(strMsg); // 将string转换成byte数组
			
			// 发送消息给server服务器
			buffOutputStream.write(buffSend);
			buffOutputStream.flush();
			
			// 接收server服务器返回的数据，
			int numRecv=buffInputStream.read(buffRecv);
			String strRecv = new String(buffRecv).substring(0, numRecv);
			System.out.println(strRecv);
			
			// 对接收的数据进行进一步分析，最主要的就是讲消息进行拆分，获得一个树的结构
			
			
			
			
			
			
			
			
			
			} catch (IOException e1) {
				System.err.println("向服务器请求设备状态数据出错，请检查以太网连接！！！");
			}
		}

	} 
	public static void main(String[] args) {
		new ControlFrame();
		/*		
		// 测试一下iP的所占字节数
		String str="/172.29.143.67";
		byte[] byteStr = str.getBytes(); // 将字符串转化成字节数组
		System.out.println(byteStr[0]);
		System.out.println(byteStr.length);
		String str1=new String(byteStr); // 将字节数组装换成字符串
		System.out.println(str1);

		*/
		
	}

}
