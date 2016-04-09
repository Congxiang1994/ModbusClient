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
	
	public JLabel laDevice = new JLabel("�豸��:", SwingConstants.RIGHT);
	public JTextField tfDevice = new JTextField("01");

	public JLabel laOrderNumber = new JLabel("�����:", SwingConstants.RIGHT);
	public JTextField tfOrderNumber = new JTextField("03");

	public JLabel laAddressHigh = new JLabel("��ַ��:", SwingConstants.RIGHT);
	public JTextField tfAddressHigh = new JTextField("00");

	public JLabel laAddressLow = new JLabel("��ַ��:", SwingConstants.RIGHT);
	public JTextField tfAddressLow = new JTextField("00");

	public JLabel laCountHigh = new JLabel("������:", SwingConstants.RIGHT);
	public JTextField tfCountHigh = new JTextField("00");

	public JLabel laCountLow = new JLabel("������:", SwingConstants.RIGHT);
	public JTextField tfCountLow = new JTextField("0A");

	public JButton btGenerate = new JButton("����modbus��������浽�·��б�");

	public JTable tableModbusOrderList = new JTable();
	public DefaultTableModel tableModel; // ����ģ�Ͷ���

	public JButton btRefresh = new JButton("����modbus������ݿ���");

	// ���췽��
	public ModbusMsgPanel() {
		super();

		
		// ��ʾ��ͷ
		String[] columnNames = { "modbu����" }; // ����
		String[][] tableVales = {}; // ����
		tableModel = new DefaultTableModel(tableVales, columnNames);
		tableModbusOrderList = new JTable(tableModel);
		JScrollPane jsptablefriends = new JScrollPane(tableModbusOrderList); // ֧�ֹ���
		jsptablefriends.setBackground(new Color(250, 222, 114));
		
		
		// ���ñ߿�
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "���ɼ�����modbus����:");
		this.setBorder(border);

		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);

		// �豸��
		gridbaglayout.setConstraints(laDevice, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laDevice);

		gridbaglayout.setConstraints(tfDevice, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfDevice);

		// �����
		gridbaglayout.setConstraints(laOrderNumber, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laOrderNumber);

		gridbaglayout.setConstraints(tfOrderNumber, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfOrderNumber);

		// ��ַ��8λ
		gridbaglayout.setConstraints(laAddressHigh, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laAddressHigh);

		gridbaglayout.setConstraints(tfAddressHigh, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfAddressHigh);

		// ��ַ��8λ
		gridbaglayout.setConstraints(laAddressLow, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laAddressLow);

		gridbaglayout.setConstraints(tfAddressLow, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfAddressLow);

		// ������8λ
		gridbaglayout.setConstraints(laCountHigh, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laCountHigh);

		gridbaglayout.setConstraints(tfCountHigh, new GBC(2).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfCountHigh);

		// ������8λ
		gridbaglayout.setConstraints(laCountLow, new GBC(1).setWeight(0, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(laCountLow);

		gridbaglayout.setConstraints(tfCountLow, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(tfCountLow);

		// ����modbus���ť
		gridbaglayout.setConstraints(btGenerate, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(btGenerate);

		// modbus�������
		gridbaglayout.setConstraints(jsptablefriends, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(jsptablefriends);

		// ����modbus���ť
		gridbaglayout.setConstraints(btRefresh, new GBC(0).setWeight(1, 0).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(btRefresh);

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