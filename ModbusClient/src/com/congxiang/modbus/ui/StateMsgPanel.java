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

public class StateMsgPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static JLabel laState = new JLabel("实时状态消息列表:",SwingConstants.LEFT);
	public static JTable tableStateMsg = new JTable();
	
	public DefaultTableModel tableModel; // 表格模型对象
	
	// 构造方法
	public StateMsgPanel() {
		
		// 显示表头
		String[] columnNames = { "时间", "终端IP地址", "设备ID", "状态" }; // 列名
		String[][] tableVales = {}; // 数据
		tableModel = new DefaultTableModel(tableVales, columnNames);
		tableStateMsg = new JTable(tableModel);
		JScrollPane jsptablefriends = new JScrollPane(tableStateMsg); // 支持滚动
		jsptablefriends.setBackground(new Color(250, 222, 114));
		
		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "设备实时状态信息列表:");
		this.setBorder(border);
		
		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);

/*		gridbaglayout.setConstraints(laState, new GBC(0).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laState);*/
		
		gridbaglayout.setConstraints(jsptablefriends, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(jsptablefriends);
		this.tableStateMsg.getColumnModel().getColumn(0).setPreferredWidth(230);
		this.tableStateMsg.getColumnModel().getColumn(1).setPreferredWidth(180);
		this.tableStateMsg.getColumnModel().getColumn(2).setPreferredWidth(150);
		this.tableStateMsg.getColumnModel().getColumn(3).setPreferredWidth(80);
	}
	
	public static void main(String[] args) {


		StateMsgPanel Panel = new StateMsgPanel();
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
