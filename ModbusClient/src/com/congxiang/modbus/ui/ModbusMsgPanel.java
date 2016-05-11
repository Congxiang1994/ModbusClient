package com.congxiang.modbus.ui;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import com.congxiang.modbus.util.GBC;

public class ModbusMsgPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	//01030000000AC5CD
	
	public JLabel laDevice = new JLabel("设备号:", SwingConstants.RIGHT);
	public JTextField tfDevice = new JTextField("02");

	public JLabel laOrderNumber = new JLabel("命令号:", SwingConstants.RIGHT);
	public JTextField tfOrderNumber = new JTextField("03");

	public JLabel laAddressHigh = new JLabel("地址高:", SwingConstants.RIGHT);
	public JTextField tfAddressHigh = new JTextField("00");

	public JLabel laAddressLow = new JLabel("地址低:", SwingConstants.RIGHT);
	public JTextField tfAddressLow = new JTextField("00");

	public JLabel laCountHigh = new JLabel("数量高:", SwingConstants.RIGHT);
	public JTextField tfCountHigh = new JTextField("00");

	public JLabel laCountLow = new JLabel("数量低:", SwingConstants.RIGHT);
	public JTextField tfCountLow = new JTextField("04");
	
	public JLabel laModbusTerminalIp = new JLabel("终端IP:", SwingConstants.RIGHT);
	public JTextField tfModbusTerminalIp = new JTextField("172.29.143.67");

	public JButton btGenerate = new JButton("生成modbus命令，并保存到下方列表");

	public JTable tableModbusOrderList = new JTable();
	public DefaultTableModel tableModel; // 表格模型对象
	public JScrollPane jsptablefriends;

	public JButton btRefresh = new JButton("更新modbus命令到数据库中");

	// 构造方法
	public ModbusMsgPanel() {
		super();


		
		// 显示表头
		String[] columnNames = { "modbus命令" ,"终端IP"}; // 列名
		String[][] tableVales = {}; // 数据
		tableModel = new DefaultTableModel(tableVales, columnNames);
		tableModbusOrderList = new JTable(tableModel);
		jsptablefriends = new JScrollPane(tableModbusOrderList); // 支持滚动
		jsptablefriends.setBackground(new Color(250, 222, 114));
		
		
		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "生成及更新modbus命令:");
		this.setBorder(border);

		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);

		// 设备号
		gridbaglayout.setConstraints(laDevice, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laDevice);

		gridbaglayout.setConstraints(tfDevice, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfDevice);

		// 命令号
		gridbaglayout.setConstraints(laOrderNumber, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laOrderNumber);

		gridbaglayout.setConstraints(tfOrderNumber, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfOrderNumber);

		// 地址高8位
		gridbaglayout.setConstraints(laAddressHigh, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laAddressHigh);

		gridbaglayout.setConstraints(tfAddressHigh, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfAddressHigh);

		// 地址低8位
		gridbaglayout.setConstraints(laAddressLow, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laAddressLow);

		gridbaglayout.setConstraints(tfAddressLow, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfAddressLow);

		// 数量高8位
		gridbaglayout.setConstraints(laCountHigh, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laCountHigh);

		gridbaglayout.setConstraints(tfCountHigh, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfCountHigh);

		// 数量低8位
		gridbaglayout.setConstraints(laCountLow, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laCountLow);

		gridbaglayout.setConstraints(tfCountLow, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfCountLow);
		
		// modbus终端IP
		gridbaglayout.setConstraints(laModbusTerminalIp, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laModbusTerminalIp);

		gridbaglayout.setConstraints(tfModbusTerminalIp, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfModbusTerminalIp);
		

		// 生成modbus命令按钮
		gridbaglayout.setConstraints(btGenerate, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(btGenerate);

		// modbus命令表格
		gridbaglayout.setConstraints(jsptablefriends, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(jsptablefriends);
/*
		// 更新modbus命令按钮
		gridbaglayout.setConstraints(btRefresh, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(btRefresh);
*/
	}

	public static void main(String[] args) {

		ModbusMsgPanel Panel = new ModbusMsgPanel();
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
