package com.congxiang.modbus.ui;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import com.congxiang.modbus.util.GBC;

public class RealTimeModbusDataPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel laModbusData = new JLabel("实时ModbusData监测数据列表:",SwingConstants.LEFT);
	public JTable tableRealTimeModbusData = new JTable();
	
	public DefaultTableModel tableModel; // 表格模型对象
	
	// 构造方法
	
	public RealTimeModbusDataPanel() {
		super();

		
		// 显示表头
		String[] columnNames = { "时间", "终端IP地址" ,"下位设备ID", "ModbusData" }; // 列名
		String[][] tableVales = {}; // 数据
		tableModel = new DefaultTableModel(tableVales, columnNames);
		
		
		tableRealTimeModbusData = new JTable(tableModel);
		JScrollPane jsptableModbusData = new JScrollPane(tableRealTimeModbusData); // 支持滚动
		jsptableModbusData.setBackground(new Color(250, 222, 114));
		
		//this.setLayout(null);
		
		// this.add(jsptablefriends);
		
		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "实时ModbusData监测数据:");
		this.setBorder(border);
		
		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);
/*		
		gridbaglayout.setConstraints(laModbusData, new GBC(0).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laModbusData);
		*/
		gridbaglayout.setConstraints(jsptableModbusData, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(jsptableModbusData);
	}
	public static void main(String[] args) {

		RealTimeModbusDataPanel Panel = new RealTimeModbusDataPanel();
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
