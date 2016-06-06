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

	public JLabel laIpAddress = new JLabel("IP��ַ:", SwingConstants.RIGHT);
	public JLabel laPort = new JLabel("�˿ں�:", SwingConstants.RIGHT);

	public JTextField tfIPAddress = new JTextField();
	public JTextField tfPort = new JTextField();

	public JButton btConnectServer = new JButton("ȷ�����ӷ���������");

	// ���췽��
	public ConnectServerPanel() {
		super();

		// ���ñ߿�
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "���ӷ�����:");
		this.setBorder(border);

		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);

		// ��ǩ��IP��ַ
		gridbaglayout.setConstraints(laIpAddress, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laIpAddress);

		// �ı���IP��ַ
		gridbaglayout.setConstraints(tfIPAddress, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfIPAddress);

		// ��ǩ���˿ں�
		gridbaglayout.setConstraints(laPort, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laPort);

		// �ı��򣺶˿ں�
		gridbaglayout.setConstraints(tfPort, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfPort);

		// ��ť���������ݿ�
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
