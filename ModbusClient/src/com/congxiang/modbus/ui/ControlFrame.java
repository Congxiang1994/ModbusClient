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
	boolean connectionStarted; // ��ʶ������������ֹ��ʱ����Ϊfalse��������ֹ���߳�
	
	// ���÷�������ַ
	JLabel laIPAddress=new JLabel("IP:", SwingConstants.RIGHT);
	JLabel laPort=new JLabel("Port:", SwingConstants.RIGHT);
	JTextArea taIPAddress = new JTextArea("127.0.0.1"); //��IP��ַ
	JTextArea taPort = new JTextArea("8000"); //���˿�
	JButton btConnectServer=new JButton("���ӷ�����"); // ���Ӱ�ť
	
	// ����modbus��Ϣ�����ڲ�ѯ��λ�豸�Ƿ�����
	JLabel laModbusMsg = new JLabel("modbus���", SwingConstants.RIGHT);
	JTextArea taModbusMsg = new JTextArea("01030000000AC5CD");
	JScrollPane jsp = new JScrollPane(taModbusMsg); //��������
	JButton btUpdateModbusMsg = new JButton("����modbus����");
	
	// �ӷ�������ȡmodbus�ն˺���λ�豸��״̬
	JLabel laStateData =new JLabel("״̬����:", SwingConstants.RIGHT);
	JTextArea taStateData = new JTextArea();
	JScrollPane jspStateData =new JScrollPane(taStateData); // ������
	JButton btGetStateData = new JButton("��ȡ״̬");
	
	// ����ͼ���
	//TreePanel treePanel = new TreePanel();
	
	ControlFrame() {
		this.setTitle("��λ������");
		this.setSize(800, 600); // ���ý����С
		
		//���ÿؼ�λ��
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
		btConnectServer.addActionListener(this); // ���ӷ�����
		
		laModbusMsg.setBounds(5,70,100,20);
		jsp.setBounds(105, 70, 140, 50);
		btUpdateModbusMsg.setBounds(5, 120, 240, 20);
		btUpdateModbusMsg.addActionListener(this);  // ����modbus��Ϣ
		
		laStateData.setBounds(5, 145, 100, 20);
		jspStateData.setBounds(105, 145, 140, 50);
		btGetStateData.setBounds(5, 200, 240, 20); 
		btGetStateData.addActionListener(this); // �Ӻ�����ݿ��ȡ����
		
		this.setLocationRelativeTo(null); // ���ý�������Ļ������ʾ
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���õ����رհ�ť�ܹ��ر�������
		this.setVisible(true); // ��ʾ����

		new Thread(new MainThread()).start(); // �������߳�
		
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
	 * ��ʱ��server����������ȡ����
	 * */
	class MonitorData implements Runnable{
		Boolean monitorDataStarted = true;
		
		public MonitorData() {
			super();
		}

		@Override
		public void run() {
			while (monitorDataStarted) {
				System.out.println("��λ������ʼ��serverȡ����");
				
			}
			
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btConnectServer) {
			/** �������ӷ����� */
			System.out.println("��λ������ʼ���ӷ�����...");
			try {
				server = new Socket(taIPAddress.getText().trim(), Integer.valueOf(taPort.getText().trim()));
				inPutStream = server.getInputStream();
				outPutStream = server.getOutputStream();

				buffInputStream = new BufferedInputStream(server.getInputStream());
				buffOutputStream = new BufferedOutputStream(server.getOutputStream());

				System.out.println("��λ������ȡ����������ɹ���");
				
				Thread.sleep(1000);// ˯��һ�룬�ȴ�server������������صĽ������ݣ������������û����ȷ��������
				
				byte[] buffID = new byte[] { 0x00 };
				buffOutputStream.write(buffID); // ����һ��0x00����ʾ�Լ�����λ��//---------------------------------------------------------------------write
				buffOutputStream.flush();
				
				Thread.sleep(500);
				
				buffInputStream.read(buffID);// ---------------------------------------------------------------------read
				
				System.out.println("��λ�����ɹ�����server�������˷��ص�������Ϣ 0x00");

			} catch (IOException e1) {
				try {
					server.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.err.println("���ӷ���������������ȴ򿪷��������򣡣���");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == btUpdateModbusMsg) {
			System.out.println("��λ������ʼ�������ݿ��е�mdobus��Ϣ...");
			try {
				// ���ı����е���Ϣ�Ƚ��в��
				String strModbusMsg = taModbusMsg.getText().trim();
				String[] strModbus = strModbusMsg.split("\n"); // ���ݻس�����Ϣ���

				// ��string����ת����һ����byte���飬Ȼ��ͨ��һ��ѭ���������ݷ��͸�server����������
				String strMsg = ""; // ��Ϣ����
				byte[] buffSend;
				for (int i = 0; i < strModbus.length; i++) {
					strMsg = "06";
					strMsg = strMsg + strModbus[i];
					buffSend = ByteUtil.hexStringToBytes(strMsg);

					// ������Ϣ��server������
					buffOutputStream.write(buffSend);// ---------------------------------------------------------------------write
					buffOutputStream.flush();
					buffInputStream.read(buffSend);// ---------------------------------------------------------------------read
					
				}
			} catch (IOException e1) {
				System.err.println("����modbus�����server����������̫�����ӣ�����");
			}
		} else if (e.getSource() == btGetStateData) {
			System.out.println("��λ������ʼ������������豸״̬����..."); // ����һ���Խ�����ͳͳȡ����
			try {
			//��Ϣ����Ϊ0x08 ������ֻ��Ҫ����һ����Ϣ���;��У�����Ҫ������������
			String strMsg = "08";
			byte[] buffSend;
			byte[] buffRecv = new byte[10000]; // ��������server���������ص��豸״̬���ݣ���ʱ�����趨Ϊ10000��֧�ֲ�����180���豸
			buffSend = ByteUtil.hexStringToBytes(strMsg); // ��stringת����byte����
			
			// ������Ϣ��server������
			buffOutputStream.write(buffSend);
			buffOutputStream.flush();
			
			// ����server���������ص����ݣ�
			int numRecv=buffInputStream.read(buffRecv);
			String strRecv = new String(buffRecv).substring(0, numRecv);
			System.out.println(strRecv);
			
			// �Խ��յ����ݽ��н�һ������������Ҫ�ľ��ǽ���Ϣ���в�֣����һ�����Ľṹ
			
			
			
			
			
			
			
			
			
			} catch (IOException e1) {
				System.err.println("������������豸״̬���ݳ���������̫�����ӣ�����");
			}
		}

	} 
	public static void main(String[] args) {
		new ControlFrame();
		/*		
		// ����һ��iP����ռ�ֽ���
		String str="/172.29.143.67";
		byte[] byteStr = str.getBytes(); // ���ַ���ת�����ֽ�����
		System.out.println(byteStr[0]);
		System.out.println(byteStr.length);
		String str1=new String(byteStr); // ���ֽ�����װ�����ַ���
		System.out.println(str1);

		*/
		
	}

}
