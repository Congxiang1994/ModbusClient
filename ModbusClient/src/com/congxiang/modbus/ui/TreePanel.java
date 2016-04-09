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
 * 物联网设备的层级关系
 * 
 * @author CongXiang 注意：这里只有：1个上位机、2个modbus终端、4个下位设备 【将来系统做大了再继续拓展】
 * */
public class TreePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JButton btClient = new JButton("上位机");

	public static  JButton btModbus0 = new JButton(); // "终端0号"
	public static  JButton btModbus1 = new JButton(); // "终端1号"

	public static  JButton btDevice00 = new JButton(); // "设备00号"
	public static  JButton btDevice01 = new JButton(); // "设备01号"
	public static  JButton btDevice10 = new JButton(); // "设备10号"
	public static  JButton btDevice11 = new JButton(); // "设备11号"

	private JLabel la1 = new JLabel();
	private JLabel la2 = new JLabel();

	// 构造方法
	public TreePanel() {
		super();
		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "树形结构图:");
		this.setBorder(border);
		
		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.weighty = 1;

		// 空白
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(la1, c);
		this.add(la1);

		// 上位机
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		gridbaglayout.setConstraints(btClient, c);
		this.add(btClient);

		// 空白
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(la2, c);
		this.add(la2);

		// 终端1号
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		gridbaglayout.setConstraints(btModbus0, c);
		this.add(btModbus0);

		// 终端2号
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 2;
		gridbaglayout.setConstraints(btModbus1, c);
		this.add(btModbus1);

		// 设备0号
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(btDevice00, c);
		this.add(btDevice00);

		// 设备1号
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(btDevice01, c);
		this.add(btDevice01);

		// 设备2号
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		gridbaglayout.setConstraints(btDevice10, c);
		this.add(btDevice10);

		// 设备3号
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
