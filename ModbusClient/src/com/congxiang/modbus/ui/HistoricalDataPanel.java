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

	public static JLabel laTimeStart = new JLabel("��ʼʱ��:", SwingConstants.RIGHT);
	public static JTextField tfTimeStart = new JTextField();

	public static JLabel laTimeEnd = new JLabel("����ʱ��:", SwingConstants.RIGHT);
	public static JTextField tfTimeEnd = new JTextField();

	public static JLabel laThresholdStart = new JLabel("��ֵ��ʼ:", SwingConstants.RIGHT);
	public static JTextField tfThresholdStart = new JTextField();

	public static JLabel laThresholdEnd = new JLabel("��ֵ����:", SwingConstants.RIGHT);
	public static JTextField tfThresholdEnd = new JTextField();

	public static JLabel laDevice = new JLabel("��λ�豸:", SwingConstants.RIGHT);
	public static JTextField tfDevice = new JTextField();

	public static JButton btStatistic = new JButton("ͳ��");

	public static JTable tableMonitorData = new JTable();
	public DefaultTableModel tableModel; // ���ģ�Ͷ���

	// ���췽��
	public HistoricalDataPanel() {

		super();
		// ��ʾ��ͷ
		String[] columnNames = { "ʱ��", "�ն�IP��ַ" ,"��λ�豸ID", "ModbusData" }; // ����
		String[][] tableVales = {}; // ����
		tableModel = new DefaultTableModel(tableVales, columnNames);
		tableMonitorData = new JTable(tableModel);
		JScrollPane jsptableMonitorData = new JScrollPane(tableMonitorData); // ֧�ֹ���
		jsptableMonitorData.setBackground(new Color(250, 222, 114));

		// ���ñ߿�
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "ͳ�Ƽ������:");
		this.setBorder(border);

		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);

		// ��ʼʱ��
		gridbaglayout.setConstraints(laTimeStart, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laTimeStart);
		
		gridbaglayout.setConstraints(tfTimeStart, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfTimeStart);
		
		// ����ʱ��
		gridbaglayout.setConstraints(laTimeEnd, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laTimeEnd);
		
		gridbaglayout.setConstraints(tfTimeEnd, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfTimeEnd);
		
		// ��ʼ��ֵ
		gridbaglayout.setConstraints(laThresholdStart, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laThresholdStart);
		
		gridbaglayout.setConstraints(tfThresholdStart, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfThresholdStart);
		
		// ������ֵ
		gridbaglayout.setConstraints(laThresholdEnd, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laThresholdEnd);
		
		gridbaglayout.setConstraints(tfThresholdEnd, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfThresholdEnd);
		
		// ��λ�豸��
		gridbaglayout.setConstraints(laDevice, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laDevice);
		
		gridbaglayout.setConstraints(tfDevice, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfDevice);
		
		// ͳ�ư�ť
		gridbaglayout.setConstraints(btStatistic, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(btStatistic);
		
		// ���������ʾ���
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
