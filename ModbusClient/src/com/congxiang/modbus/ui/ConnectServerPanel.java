package com.congxiang.modbus.ui;

import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.congxiang.modbus.util.GBC;

public class ConnectServerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JLabel laIpAddress = new JLabel("IP地址:", SwingConstants.RIGHT);
	public JLabel laPort = new JLabel("端口号:", SwingConstants.RIGHT);

	public JTextField tfIPAddress = new JTextField();
	public JTextField tfPort = new JTextField();

	public JButton btConnectServer = new JButton("确认连接服务器程序");

	// 构造方法
	public ConnectServerPanel() {
		super();

		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "连接服务器:");
		this.setBorder(border);

		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);

		// 标签：IP地址
		gridbaglayout.setConstraints(laIpAddress, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laIpAddress);

		// 文本框：IP地址
		gridbaglayout.setConstraints(tfIPAddress, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfIPAddress);

		// 标签：端口号
		gridbaglayout.setConstraints(laPort, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laPort);

		// 文本框：端口号
		gridbaglayout.setConstraints(tfPort, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfPort);

		// 按钮：连接数据库
		gridbaglayout.setConstraints(btConnectServer, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(btConnectServer);

		tfIPAddress.setText("127.0.0.1");
		tfPort.setText("9000");

	}

	public static void main(String[] args) {

		ConnectServerPanel Panel = new ConnectServerPanel();
		JFrame frame = new JFrame();
		frame.add(Panel);

		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

}
