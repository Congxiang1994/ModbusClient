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

public class HistoricalDataPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JLabel laTimeStart = new JLabel("起始时间:", SwingConstants.RIGHT);
	public static JTextField tfTimeStart = new JTextField();

	public static JLabel laTimeEnd = new JLabel("结束时间:", SwingConstants.RIGHT);
	public static JTextField tfTimeEnd = new JTextField();

	public static JLabel laThresholdStart = new JLabel("阈值起始:", SwingConstants.RIGHT);
	public static JTextField tfThresholdStart = new JTextField();

	public static JLabel laThresholdEnd = new JLabel("阈值结束:", SwingConstants.RIGHT);
	public static JTextField tfThresholdEnd = new JTextField();

	public static JLabel laDevice = new JLabel("下位设备:", SwingConstants.RIGHT);
	public static JTextField tfDevice = new JTextField();

	public static JButton btStatistic = new JButton("统计");

	public static JTable tableMonitorData = new JTable();
	public DefaultTableModel tableModel; // 表格模型对象

	// 构造方法
	public HistoricalDataPanel() {

		super();
		// 显示表头
		String[] columnNames = { "时间", "终端IP地址" ,"下位设备ID", "ModbusData" }; // 列名
		String[][] tableVales = {}; // 数据
		tableModel = new DefaultTableModel(tableVales, columnNames);
		tableMonitorData = new JTable(tableModel);
		JScrollPane jsptableMonitorData = new JScrollPane(tableMonitorData); // 支持滚动
		jsptableMonitorData.setBackground(new Color(250, 222, 114));

		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "统计监测数据:");
		this.setBorder(border);

		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);

		// 起始时间
		gridbaglayout.setConstraints(laTimeStart, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laTimeStart);
		
		gridbaglayout.setConstraints(tfTimeStart, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfTimeStart);
		
		// 结束时间
		gridbaglayout.setConstraints(laTimeEnd, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laTimeEnd);
		
		gridbaglayout.setConstraints(tfTimeEnd, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfTimeEnd);
		
		// 起始阈值
		gridbaglayout.setConstraints(laThresholdStart, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laThresholdStart);
		
		gridbaglayout.setConstraints(tfThresholdStart, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfThresholdStart);
		
		// 结束阈值
		gridbaglayout.setConstraints(laThresholdEnd, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laThresholdEnd);
		
		gridbaglayout.setConstraints(tfThresholdEnd, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfThresholdEnd);
		
		// 下位设备号
		gridbaglayout.setConstraints(laDevice, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laDevice);
		
		gridbaglayout.setConstraints(tfDevice, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfDevice);
		
		// 统计按钮
		gridbaglayout.setConstraints(btStatistic, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(btStatistic);
		
		// 监测数据显示表格
		gridbaglayout.setConstraints(jsptableMonitorData, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(jsptableMonitorData);
	}

	public static void main(String[] args) {

		HistoricalDataPanel Panel = new HistoricalDataPanel();
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
