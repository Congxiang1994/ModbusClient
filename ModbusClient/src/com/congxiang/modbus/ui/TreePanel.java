package com.congxiang.modbus.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * �������豸�Ĳ㼶��ϵ
 * 
 * @author CongXiang ע�⣺����ֻ�У�1����λ����2��modbus�նˡ�4����λ�豸 ������ϵͳ�������ټ�����չ��
 * */
public class TreePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JButton btClient = new JButton("��λ��");

	public static  JButton btModbus0 = new JButton(); // "�ն�0��"
	public static  JButton btModbus1 = new JButton(); // "�ն�1��"

	public static  JButton btDevice00 = new JButton(); // "�豸00��"
	public static  JButton btDevice01 = new JButton(); // "�豸01��"
	public static  JButton btDevice10 = new JButton(); // "�豸10��"
	public static  JButton btDevice11 = new JButton(); // "�豸11��"

	private JLabel la1 = new JLabel();
	private JLabel la2 = new JLabel();

	// ���췽��
	public TreePanel() {
		super();
		// ���ñ߿�
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "���νṹͼ:");
		this.setBorder(border);
		
		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.weighty = 1;

		// �հ�
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(la1, c);
		this.add(la1);

		// ��λ��
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		gridbaglayout.setConstraints(btClient, c);
		this.add(btClient);

		// �հ�
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(la2, c);
		this.add(la2);

		// �ն�1��
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		gridbaglayout.setConstraints(btModbus0, c);
		this.add(btModbus0);

		// �ն�2��
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 2;
		gridbaglayout.setConstraints(btModbus1, c);
		this.add(btModbus1);

		// �豸0��
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(btDevice00, c);
		this.add(btDevice00);

		// �豸1��
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(btDevice01, c);
		this.add(btDevice01);

		// �豸2��
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(btDevice10, c);
		this.add(btDevice10);

		// �豸3��
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(btDevice11, c);
		this.add(btDevice11);
	

	}

	public static void main(String[] args) {
		TreePanel treePanel = new TreePanel();
		JFrame frame = new JFrame();
		frame.add(treePanel);

		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
