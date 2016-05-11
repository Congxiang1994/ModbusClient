package com.congxiang.modbus.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import com.congxiang.modbus.util.ByteUtil;
import com.congxiang.modbus.util.CRC16;
import com.congxiang.modbus.util.GBC;
import com.congxiang.modbus.util.Node;
import com.congxiang.modbus.util.UtilTool;

/**
 * ��λ������
 * 
 * @author CongXiang
 * */

public class Application extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// ����
	public Socket server;
	public InputStream inPutStream;
	public OutputStream outPutStream;
	public BufferedInputStream buffInputStream;
	public BufferedOutputStream buffOutputStream;
	
	public boolean isConnectServer ;

	public DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode("��λ��");

	// ������
	public TreePanel treePanel = new TreePanel();
	public StateTreePanel stateTreePanel = new StateTreePanel(nodeRoot);
	public GraphPanel graphPanel = new GraphPanel();
	public StateMsgPanel stateMsgPanel = new StateMsgPanel();
	public ConnectServerPanel connectServerPanel = new ConnectServerPanel();
	public ModbusMsgPanel modbusMsgPanel = new ModbusMsgPanel();
	public HistoricalDataPanel historicalDataPanel = new HistoricalDataPanel();
	public RealTimeModbusDataPanel realTimeModbusDataPanel = new RealTimeModbusDataPanel();

	// �ײ�״̬��
	public JTextField tfState = new JTextField("״̬��:");

	// �Ҽ��˵�
	public MenuItem menuItem = new MenuItem("ɾ��");

	// ���췽��
	public Application() {
		
		isConnectServer = false; // ��δ���ӷ�����

		/* ��λ�����ƽ��沼��--------------------------------------------------------------------------------------------------- */
/*		
		// ����һ���
		Panel panelThreeInOne = new Panel();
		GridBagLayout gridBagLayoutThreeInOne = new GridBagLayout();
		panelThreeInOne.setLayout(gridBagLayoutThreeInOne);
		// �豸״̬�б�
		gridBagLayoutThreeInOne.setConstraints(stateMsgPanel, new GBC(0, 0, 2, 2).setWeight(1, 1).setFill(GBC.BOTH));
		panelThreeInOne.add(stateMsgPanel);
		// ���ӷ�����
		gridBagLayoutThreeInOne.setConstraints(connectServerPanel, new GBC(0, 2, 2, 1).setWeight(1, 0).setFill(GBC.BOTH));
		panelThreeInOne.add(connectServerPanel);
		// ���ɲ�����modbus����
		gridBagLayoutThreeInOne.setConstraints(modbusMsgPanel, new GBC(2, 0, 0, 0).setWeight(1, 1).setFill(GBC.BOTH));
		panelThreeInOne.add(modbusMsgPanel);

		// �ĺ�һ���
		Panel panelFourInOne = new Panel();
		GridLayout gridLayout = new GridLayout(2, 2);
		panelFourInOne.setLayout(gridLayout);

		panelFourInOne.add(stateTreePanel);
		panelFourInOne.add(realTimeModbusDataPanel); // �����滻��һ��modbusdataʵʱ���������ʾ�ı�����
		panelFourInOne.add(panelThreeInOne);
		panelFourInOne.add(historicalDataPanel);
		
	*/	
		// ����һ��壺���ӷ����� + ����modbus��������
		Panel panelConnectAndOrder = new Panel();
		GridBagLayout gridBagLayoutThreeInOne = new GridBagLayout();
		panelConnectAndOrder.setLayout(gridBagLayoutThreeInOne);
		gridBagLayoutThreeInOne.setConstraints(connectServerPanel, new GBC(0, 0, 2, 1).setWeight(1, 0).setFill(GBC.BOTH));// ���ӷ�����
		panelConnectAndOrder.add(connectServerPanel);
		gridBagLayoutThreeInOne.setConstraints(modbusMsgPanel, new GBC(0, 2, 2, 2).setWeight(1, 1).setFill(GBC.BOTH));// �豸״̬�б�
		panelConnectAndOrder.add(modbusMsgPanel);

		
		// ����һ��壺�����ӷ����� + ����modbus�������ݡ� + ״̬�� + ����modbus����
		Panel panelThreeInOneAndStateTree = new Panel();
		GridLayout gridLayout = new GridLayout(1, 2);
		panelThreeInOneAndStateTree.setLayout(gridLayout);
		
		panelThreeInOneAndStateTree.add(panelConnectAndOrder);
		panelThreeInOneAndStateTree.add(stateTreePanel);
		//panelThreeInOne.add(stateMsgPanel); 
		
		// ����һ��壺�������+ʵʱ״̬��Ϣ
		Panel panelThreeInOneAndRealTimeState = new Panel();
		GridLayout gridLayoutRealTimeState = new GridLayout(1, 2);
		panelThreeInOneAndRealTimeState.setLayout(gridLayoutRealTimeState);
		
		panelThreeInOneAndRealTimeState.add(panelThreeInOneAndStateTree);
		panelThreeInOneAndRealTimeState.add(stateMsgPanel);
		
		// ����һ��壺��������һ��� + ʵʱ modbusdata����������
		
		Panel panelTwoInOne = new Panel();
		GridLayout gridLayoutTwo = new GridLayout(2, 1);
		panelTwoInOne.setLayout(gridLayoutTwo);

		panelTwoInOne.add(panelThreeInOneAndRealTimeState);
		panelTwoInOne.add(realTimeModbusDataPanel); 
		
		// ��ӵײ�״̬��Ϣ����
		GridBagLayout gridBagLayoutMain = new GridBagLayout();
		this.setLayout(gridBagLayoutMain);

		gridBagLayoutMain.setConstraints(panelTwoInOne, new GBC(0, 0, 8, 8).setWeight(1, 1).setFill(GBC.BOTH));
		this.add(panelTwoInOne);

		tfState.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		gridBagLayoutMain.setConstraints(tfState, new GBC(0, 8, 8, 1).setWeight(1, 0).setFill(GBC.BOTH));
		this.add(tfState);
		/* -------------------------------------------------------------------------------------------------------------------- */
		
		/* �����Ϣ��Ӧ�¼�------------------------------------------------------------------------------------------------------*/

		// ���ӷ�������ť
		connectServerPanel.btConnectServer.addActionListener(this);

		// ����modbus���ť
		modbusMsgPanel.btGenerate.addActionListener(this);

		// ����ɾ��modbus�����б���modbus������Ҽ��˵�
		PopupMenu pop = new PopupMenu();
		// MenuItem menuItem = new MenuItem("ɾ��");
		pop.add(menuItem);
		modbusMsgPanel.tableModbusOrderList.add(pop);
		modbusMsgPanel.tableModbusOrderList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = modbusMsgPanel.tableModbusOrderList.rowAtPoint(e.getPoint());
					modbusMsgPanel.tableModbusOrderList.changeSelection(row, 0, false, false);
					pop.show(modbusMsgPanel.tableModbusOrderList, e.getX(), e.getY());
				}
			}
		});
		menuItem.addActionListener(this);

		// �������ݿ���modbus����İ�ť
		modbusMsgPanel.btRefresh.addActionListener(this);
		/* -------------------------------------------------------------------------------------------------------------------- */
		// ����ʵʱmodbus������ݵ��п�
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(0).setPreferredWidth(200);
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(1).setPreferredWidth(200);
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(2).setPreferredWidth(200);
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(3).setPreferredWidth(600);
		/* -------------------------------------------------------------------------------------------------------------------- */
		
		/* -------------------------------------------------------------------------------------------------------------------- */
		
		/* ���ô��ڵ�����------------------------------------------------------------------------------------------------------- */
		this.setTitle("��λ��Ӧ�ó���");
		this.pack();
		this.setSize(1200, 900);
		this.setLocationRelativeTo(null); // ���ý�������Ļ������ʾ
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���õ����رհ�ť�ܹ��ر�������
		this.setVisible(true); // ��ʾ����
		/* -------------------------------------------------------------------------------------------------------------------- */
		// ���ð�ť״̬
		this.buttonState(true, false);
	}

	/* ��Ϣ��Ӧ�¼� */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectServerPanel.btConnectServer) { /* �������ӷ����� -----------------------------------------*/
			printInformation(1, "��ʼ���ӷ���������...");
			// ���ð�ť״̬
			this.buttonState(false, false);
			try {
				// 1.����socketͨ�ŵı�Ҫ����
				server = new Socket(connectServerPanel.tfIPAddress.getText().trim(), Integer.valueOf(connectServerPanel.tfPort.getText().trim()));
				inPutStream = server.getInputStream();
				outPutStream = server.getOutputStream();
				buffInputStream = new BufferedInputStream(server.getInputStream());
				buffOutputStream = new BufferedOutputStream(server.getOutputStream());
				printInformation(1, "���ӷ������ɹ���");

				// 2.�����������ݵ��̣߳����������߳� 
				ReceiveMsgFromServer receiveMsgFromServer = new ReceiveMsgFromServer(server, buffInputStream, buffOutputStream);
				new Thread(receiveMsgFromServer).start(); // �����������ݵ��߳�
				printInformation(1, "�����������ݵ��̡߳�");

				Thread.sleep(1000);// ˯��һ�룬�ȴ�server������������صĽ������ݣ������������û����ȷ��������

				// 3.��server����0x00����ʶ�Լ�����λ��
				byte[] buffID = new byte[] { 0x00 };
				/*
				buffOutputStream.write(buffID); // ����һ��0x00����ʾ�Լ�����λ��//---------------------------------------------------------------------write
				buffOutputStream.flush();
				 */
				sendMsg(buffOutputStream, buffID,1);// ����һ��0x00����ʾ�Լ�����λ��//---------------------------------------------------------------------write
				
				Thread.sleep(500);
				

/*				
				// 4.��������ʵʱmodbusdata������ݵ��̣߳����������߳�
				RequestRealTimeModbusData RequestRealTimeModbusData = new RequestRealTimeModbusData(server, buffInputStream, buffOutputStream);
				new Thread(RequestRealTimeModbusData).start();
*/
				// 5.��ʶ�Ѿ��ɹ�����server������
				isConnectServer = true;
				
			
				// 6.���������豸״̬���̣߳����������߳�
				RequestEquipmentState requestEquipmentState = new RequestEquipmentState(server, buffInputStream, buffOutputStream);
				new Thread(requestEquipmentState).start();
				// ���ð�ť״̬
				this.buttonState(false, true);
				
				
			} catch (NumberFormatException | IOException | InterruptedException e1) {
				printInformation(-1, "���棺���ӷ��������ִ��������������Ӽ����Ӳ���������");
				// ���ð�ť״̬
				this.buttonState(true, false);
				// e1.printStackTrace();
			}
			/* -------------------------------------------------------------------------------------------------------------------- */
		} else if (e.getSource() == modbusMsgPanel.btGenerate) { /* ����modbus���� --------------------------------------------------*/
			
			// 1.����modbusorder(������CRCУ�鲿��)��01030000000AC5CD
			String strModbusMsg = "";
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfDevice.getText().substring(0, 2).trim(); // �豸��
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfOrderNumber.getText().substring(0, 2).trim(); // ������
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfAddressHigh.getText().substring(0, 2).trim(); // �Ĵ�����ַ��8λ
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfAddressLow.getText().substring(0, 2).trim(); // �Ĵ�����ַ��8λ
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfCountHigh.getText().substring(0, 2).trim(); // �Ĵ���������8λ
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfCountLow.getText().substring(0, 2).trim(); // �Ĵ��������ĵ�8λ

			// 2.����CRC16У���룬ע��CRCУ���8λ��ǰ����8λ�ں�
			int crc = CRC16.calcCrc16(ByteUtil.hexStringToBytes(strModbusMsg));
			String strCRC = String.format("%04x", crc).toUpperCase();
			//System.out.println(strCRC);
			strModbusMsg = strModbusMsg + strCRC.substring(2, 4); // CRCУ���8λ
			strModbusMsg = strModbusMsg + strCRC.substring(0, 2); // CRCУ���8λ

			// 3.�����������
			boolean isModbusMsgExist = false;
			for (int i = 0; i < modbusMsgPanel.tableModel.getRowCount(); i++) {
				if (strModbusMsg.equals(modbusMsgPanel.tableModel.getValueAt(i, 0))) {
					printInformation(-1, "���棺��modbus�����Ѿ����ֹ��ˣ�����");
					isModbusMsgExist = true;
					break; // ����Ĵ��벻��Ҫִ��
				}
			}
			
			// 4.�������ɵ�modbus����ŵ��·��б���,����������������>>>>>>>>>>>������Ҫ�ж��Ƿ��������ݿ⣬���û�еĻ����������modbus����
			if (isModbusMsgExist == false && isConnectServer == true) {
				String strTerminalIP;
				String str[] = new String[2];
				str[0] = strModbusMsg;
				strTerminalIP = "/"+modbusMsgPanel.tfModbusTerminalIp.getText().trim();
				str[1] = strTerminalIP;
				modbusMsgPanel.tableModel.addRow(str);
				//modbusMsgPanel.tableModbusOrderList.setRowSelectionInterval(modbusMsgPanel.tableModel.getRowCount()-1, modbusMsgPanel.tableModel.getRowCount()-1);;
				
				/* �ù����Զ��������һ�� */
				int rowCount = modbusMsgPanel.tableModbusOrderList.getRowCount();
				modbusMsgPanel.tableModbusOrderList.getSelectionModel().setSelectionInterval(rowCount-1, rowCount-1);
				Rectangle rect = modbusMsgPanel.tableModbusOrderList.getCellRect(rowCount-1, 0, true);
				//modbusMsgPanel.tableModbusOrderList.repaint(); //����Ҫ�Ļ�
				modbusMsgPanel.tableModbusOrderList.updateUI();//����Ҫ�Ļ�
				modbusMsgPanel.tableModbusOrderList.scrollRectToVisible(rect);
				
				printInformation(1, "���modbus����ɹ���");
				/**
				 * >>>���ڵ����⣺ 1.���ܽ��²�������ݷ��ڵ�һ�� 2.���ܹ�ʶ��Ƿ��ַ�
				 * */
				// 4-5.��modbus�ն˵�IP��ַ��ӵ�modbus��Ϣ��
				strModbusMsg = strModbusMsg + ByteUtil.intToString(strTerminalIP.length()) + strTerminalIP;
				
				// 5.��װ���modbus�������Ϣ����0x0F,��Ϣ���ݣ�0x0F+modbus����
				String strInsertModbusOrder = new String(new byte[] {0x0F}) + strModbusMsg; 
				byte[] buffSendInsertModbusOrder = strInsertModbusOrder.getBytes();
				// 6.����Ϣ���͸�Server����
				try {
					sendMsg(buffOutputStream, buffSendInsertModbusOrder, buffSendInsertModbusOrder.length);// ---------------------------------------------------------------------write
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}else{
				printInformation(-1, "���棺��δ����server��������������Ѿ���������modbus����������modbus�������");
			}
			/* -------------------------------------------------------------------------------------------------------------------- */
		} else if (e.getSource() == modbusMsgPanel.btRefresh) { /* ����modbus������ݿ��� ----------------------------------------- */
			
			// 1.���ж�modbus�����б����Ƿ�������,�Ƿ��Ѿ��ɹ����ӷ�����
			if ((modbusMsgPanel.tableModel.getRowCount() > 0) && isConnectServer == true) {
				
				// 2.��modbus�б��е����ݴ��һ��string���飬String[] str3= (String[])vec.get(0);
				Object[] objModbus = modbusMsgPanel.tableModel.getDataVector().toArray();
				String[] strModbus = new String[objModbus.length];
				for (int i = 0; i < objModbus.length; i++) {
					strModbus[i] = objModbus[i].toString().substring(1, 17); // modbus���Ϊ16����ȡ��ԭ��ȥ��ǰ�������ţ�[01030000110AC99D]
				}
				printInformation(1, Arrays.toString(strModbus));
				
				// 3.��װ��Ҫ���͵�modbusorder��������modbusorder��װ��һ���͡�
				String strMsg = "06"; // ��Ϣ����
				byte[] buffSend;
				try {
					for (int i = 0; i < strModbus.length; i++) {
						strMsg = strMsg + strModbus[i];
					}

					buffSend = ByteUtil.hexStringToBytes(strMsg);
					/*
					buffOutputStream.write(buffSend);// ---------------------------------------------------------------------write
					buffOutputStream.flush();
					*/
					// 4.����װ�õ�modbusorder���͸�server����������
					sendMsg(buffOutputStream, buffSend, buffSend.length);// ---------------------------------------------------------------------write
					
					// printInformation(1, "����modbus�����server����������ɹ���");
				} catch (IOException e1) {
					printInformation(-1, "���棺����modbus�����server����������̫�����ӣ�����");
				}
			} else if (modbusMsgPanel.tableModel.getRowCount() <= 0) {
				printInformation(-1, "���棺modbus�����б���û�����ݣ�����");
			} else if(isConnectServer == false){
				printInformation(-1, "���棺��δ����server���������򣡣���");
			}
			/* -------------------------------------------------------------------------------------------------------------------- */
		} else if (e.getSource() == menuItem) { /* �Ҽ��˵���modbus�����б��е��Ҽ�ɾ������ ------------------------------------------ */
			
			String strTerminalIp;
			
			// 1.��ȡѡ�е�������
			int selectedRow = modbusMsgPanel.tableModbusOrderList.getSelectedRow(); // ���ѡ��������
			printInformation(1, "ɾ����" + selectedRow + "�е�modbus����");			
			
			// 2.��װ���modbus�������Ϣ����0x10,��Ϣ���ݣ�0x10+modbus����
			String strDeleteModbusOrder = new String(new byte[] {0x10}) + modbusMsgPanel.tableModel.getValueAt(selectedRow, 0); 
			strTerminalIp = (String) modbusMsgPanel.tableModel.getValueAt(selectedRow, 1);
			strDeleteModbusOrder = strDeleteModbusOrder + ByteUtil.intToString(strTerminalIp.length()) + strTerminalIp;
			byte[] buffSendDeleteModbusOrder = strDeleteModbusOrder.getBytes();

			// 3.����Ϣ���͸�Server����
			try {
				sendMsg(buffOutputStream, buffSendDeleteModbusOrder, buffSendDeleteModbusOrder.length);// ---------------------------------------------------------------------write
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// 4.ɾ��ѡ����
			modbusMsgPanel.tableModel.removeRow(selectedRow);
		}
	}



	/**
	 * �����server���յ���������
	 * 
	 * @author CongXiang ˼·�� 1.������Ϣ���ն��������洦������
	 * */
	class ReceiveMsgFromServer implements Runnable {
		boolean receiveMsgFromServerStarted;
		private Socket socket; // �׽���
		private BufferedInputStream buffInputStream;
		private BufferedOutputStream buffOutputStream;

		// ���췽��
		public ReceiveMsgFromServer(Socket s, InputStream inPutStream, OutputStream outPutStream) {
			this.socket = s;
			this.buffInputStream = new BufferedInputStream(inPutStream);
			this.buffOutputStream = new BufferedOutputStream(outPutStream);
		}

		@Override
		public void run() {
			printInformation(1, "��ʼ������������߳�...");
			receiveMsgFromServerStarted = true;
			
			while (receiveMsgFromServerStarted) {
				// printInformation(1, "����ѭ������ʼ������Ϣ...");
				byte[] buffRecv = new byte[10000];// ���յ�һ���ֽڵĻ�����
				try {
					/*
					int numRecv = this.buffInputStream.read(buffRecv);// ---------------------------------------------------------------------read
					*/
					int numRecv = recvMsg(this.buffInputStream, buffRecv); // ---------------------------------------------------------------------read
					
					// System.out.println("���յ��ַ�����Ϊ��"+numRecv);
					// �жϴ�server�������˽��ܵ������ǲ���Ϊ��
					if (numRecv < 0) {
						buttonState(true, false);
						printInformation(-1, "���棺������Ϣ��������");
						try {
							this.socket.close();
						} catch (IOException e1) {
						}
						try {
							this.buffInputStream.close();
						} catch (IOException e1) {
						}
						try {
							this.buffOutputStream.close();
						} catch (IOException e1) {
						}
						receiveMsgFromServerStarted = false;
						break;
					}

					// ���ｫ����firstBuffer�����������в�ͬ�����ݴ���
					//printInformation(1, "�����������ݽ��յ��ַ��ж���Ϣ����..." + buffRecv[0]);
					switch (buffRecv[0]) {
					
					case 0x00:
						printInformation(1, "��Ϣ���ͣ�0x00:���ӷ������ɹ������ɹ�����0x00������������ʶ���Լ���ݡ�");
						break;
						
					case 0x06:
						printInformation(1, "��Ϣ���ͣ�0x06:����modbus�����server����������ɹ���");
						break;
						
					case 0x08:
						printInformation(1, "��Ϣ���ͣ�0x08:���յ�server���ص��豸ʵʱ״̬��Ϣ");

						/* �Խ��յ��豸ʵʱ״̬��Ϣ���д��� */
						byte[] buffRecvReal = new byte[numRecv];
						for (int i = 0; i < numRecv; i++) {
							buffRecvReal[i] = buffRecv[i];
						}
						String strRecvReal = new String(buffRecvReal); // �����0x08��Ϣ����
						Node node = UtilTool.stringToNodeTree(strRecvReal);

						// ���豸״̬��ʾ�����νṹͼ��
						nodeRoot.removeAllChildren(); // ��ɾ�����ڵ�������ӽڵ�
						if (node.hasChild()) { // �����modbus�ն�
							for (int i = 0; i < node.getChilds().size(); i++) { // ����modbus�ն��б�
								DefaultMutableTreeNode nodeModbus = new DefaultMutableTreeNode(node.getChilds().get(i).getName());
								nodeRoot.add(nodeModbus);
								if (node.getChilds().get(i).hasChild()) {
									for (int j = 0; j < node.getChilds().get(i).getChilds().size(); j++) { // ������λ�豸�б�
										DefaultMutableTreeNode nodeDevice = new DefaultMutableTreeNode(node.getChilds().get(i).getChilds().get(j).getName());
										nodeModbus.add(nodeDevice);
									}
								}
							}
						}
						stateTreePanel.treeModel.reload(nodeRoot); // ˢ�����νṹͼ
						UtilTool.expandTree(stateTreePanel.tree); // չ���������νṹͼ�еĽڵ�
						//printInformation(1, "��Ϣ���ͣ�0x08:ˢ�����νṹͼ�ɹ�");
						byte[] buff08 = new byte[] { 0x08 };
						sendMsg(buffOutputStream, buff08, 1);//---------------------------------------------------------------------write
						break;
						
					case 0x0B:
						printInformation(1, "��Ϣ���ͣ�0x0B:���յ�server���ص��豸ʵʱmodbusdata�������");
						String[] strModbusDataMsg = new String[4]; // �����������4������
						if(numRecv > 1){
							// 1.�ڹ�������дһ������������һ�����飺ʱ�� + modbus�ն�IP��ַ + �豸ID + modbusdata�������
							strModbusDataMsg = byteModbusDataToStringArray(buffRecv, numRecv);
							printInformation(1, "��Ϣ���ͣ�0x0B:���յ�modbusdata�������:"+strModbusDataMsg[0]+","+strModbusDataMsg[1]+","+strModbusDataMsg[2]+","+strModbusDataMsg[3]);
							
							// 2.��modbusdata������ݼӽ�modbusdataʵʱ��������б���
							realTimeModbusDataPanel.tableModel.addRow(strModbusDataMsg);
							
						}
						byte[] buff0B = new byte[] { 0x0B };
						sendMsg(buffOutputStream, buff0B, 1);//---------------------------------------------------------------------write

						break;
						
					case 0x0C:
						printInformation(1, "��Ϣ���ͣ�0x0C:���յ�server���صĵ����豸ʵʱ״̬��Ϣ");
						String[] strModbusStateData = new String[4]; // �����������4������
						if(numRecv > 1){
							// 1.�ڹ�������дһ������������һ�����飺ʱ�� + modbus�ն�IP��ַ + �豸ID + �豸״̬
							strModbusStateData = byteDeviceStateToStringArray(buffRecv, numRecv);
							printInformation(1, "��Ϣ���ͣ�0x0C:���յ������豸״̬��Ϣ:"+strModbusStateData[0]+","+strModbusStateData[1]+","+strModbusStateData[2]+","+strModbusStateData[3]);
							// 2.��modbusdata������ݼӽ�modbusdataʵʱ��������б���
							stateMsgPanel.tableModel.addRow(strModbusStateData);
							
						}
						
						break;
						
					case 0x0D:
						printInformation(1, "��Ϣ���ͣ�0x0D:���յ�server���ص��豸ʵʱ״̬��");
						/* �Խ��յ��豸ʵʱ״̬��Ϣ���д��� */
						byte[] buffRecvFirst = new byte[numRecv];
						for (int i = 0; i < numRecv; i++) {
							buffRecvFirst[i] = buffRecv[i];
						}
						String strRecvFirst = new String(buffRecvFirst); // �����0x08��Ϣ����
						Node nodeFirst = UtilTool.stringToNodeTree(strRecvFirst);

						// ���豸״̬��ʾ�����νṹͼ��
						nodeRoot.removeAllChildren(); // ��ɾ�����ڵ�������ӽڵ�
						if (nodeFirst.hasChild()) { // �����modbus�ն�
							for (int i = 0; i < nodeFirst.getChilds().size(); i++) { // ����modbus�ն��б�
								DefaultMutableTreeNode nodeModbus = new DefaultMutableTreeNode(nodeFirst.getChilds().get(i).getName());
								nodeRoot.add(nodeModbus);
								if (nodeFirst.getChilds().get(i).hasChild()) {
									for (int j = 0; j < nodeFirst.getChilds().get(i).getChilds().size(); j++) { // ������λ�豸�б�
										DefaultMutableTreeNode nodeDevice = new DefaultMutableTreeNode(nodeFirst.getChilds().get(i).getChilds().get(j).getName());
										nodeModbus.add(nodeDevice);
									}
								}
							}
						}
						stateTreePanel.treeModel.reload(nodeRoot); // ˢ�����νṹͼ
						UtilTool.expandTree(stateTreePanel.tree); // չ���������νṹͼ�еĽڵ�
						//printInformation(1, "��Ϣ���ͣ�0x08:ˢ�����νṹͼ�ɹ�");
						break;
						
					case 0x0E:
						printInformation(1, "��Ϣ���ͣ�0x0E:���յ�server���ص�modbus����");
						String strBuffRecv = new String(buffRecv);
						// 1.�������յ�������
						String strModbusOrder = strBuffRecv.substring(1, 17); // ֱ�ӽ��ֽ����鰴��ascll��ķ�ʽת����string����
						//System.out.println(strModbusOrder);
						
						// 2.��������ж��б����Ƿ��Ѿ���������modbus����
						boolean isModbusMsgExist = false;
						for (int i = 0; i < modbusMsgPanel.tableModel.getRowCount(); i++) {
							if (strModbusOrder.equals(modbusMsgPanel.tableModel.getValueAt(i, 0))) {
								printInformation(-1, "���棺��modbus�����Ѿ����ֹ��ˣ�����");
								isModbusMsgExist = true;
							}
						}
						
						// 3.��modbus�����б������һ������
						if (isModbusMsgExist == false) {
							String str[] = new String[2];
							str[0] = strModbusOrder;
							int lengthOfTerminalIp = Integer.valueOf(strBuffRecv.substring(17,19));
							str[1] = strBuffRecv.substring(19, 19+lengthOfTerminalIp);
							modbusMsgPanel.tableModel.addRow(str);
							printInformation(1, "���modbus����ɹ���");
							
							
						}
						
						// 4.����Ϣ���ͷ��ظ�Server���򣬣���Ϣ�����������͵����������
						byte[] buff0E = new byte[] { 0x0E };
						sendMsg(buffOutputStream, buff0E, 1);//---------------------------------------------------------------------write
						break;
						
					case 0x0F:
						printInformation(1, "��Ϣ���ͣ�0x0F:��λ�����modbus����ɹ���");
						break;
						
					case 0x10:
						printInformation(1, "��Ϣ���ͣ�0x10:��λ��ɾ��modbus����ɹ���");
						break;
						
					default:
						printInformation(-1, "���棬���յ�δ֪��Ϣ���ͣ�����");
						break;
					}
				} catch (IOException e) {
					// ���ð�ť״̬
					buttonState(true, false);
					printInformation(-1, "���棬�����Ѿ��Ͽ���server��������ӣ�����");
					receiveMsgFromServerStarted = false;
					try {
						socket.close();
						buffInputStream.close();
						buffOutputStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		}
	}

	
	/**��server���������������豸��״̬��0x0D  �����߳�------------------------------------------------------------------------------------------------ */
	/** -------------------------------------------------------------------------------------------------------------------------------------------- */
	class RequestEquipmentState implements Runnable {
		//boolean requestEquipmentStateStarted;
		private Socket socket; // �׽���
		private BufferedInputStream buffInputStream;
		private BufferedOutputStream buffOutputStream;

		// ���췽��
		public RequestEquipmentState(Socket s, InputStream inPutStream, OutputStream outPutStream) {
			this.socket = s;
			this.buffInputStream = new BufferedInputStream(inPutStream);
			this.buffOutputStream = new BufferedOutputStream(outPutStream);
		}

		@Override
		public void run() {
			//requestEquipmentStateStarted = true;
			printInformation(1, "�����߳�->�����豸״̬�����������豸״̬���̳߳ɹ���");
			try {
				//while (requestEquipmentStateStarted) {
					printInformation(1, "�����߳�->�����豸״̬����ʼ��������豸��״̬...");

					// ��Ϣ����Ϊ0x0D ������ֻ��Ҫ����һ����Ϣ���;��У�����Ҫ������������
					String strMsg = "0D";
					byte[] buffSend;
					buffSend = ByteUtil.hexStringToBytes(strMsg); // ��stringת����byte����
					// ������Ϣ��server������
					/*
					this.buffOutputStream.write(buffSend);
					this.buffOutputStream.flush();
					*/
					sendMsg(this.buffOutputStream, buffSend, buffSend.length); //---------------------------------------------------------------------write

					Thread.sleep(10000); // ��ͣ�����ӣ�����Ҫһֱ����״̬
				//}
			} catch (InterruptedException | IOException e) {
				printInformation(-1, "�����߳�->�����豸״̬�����棬�����Ѿ��Ͽ���server��������ӣ�����");
				//e.printStackTrace();
			}
		}
	}
	
	/**��server��������������ʵʱ���ݣ�0x0B  �����߳�------------------------------------------------------------------------------------------------ */
	/** ------------------------------------------------------------------------------------------------------------------------------------------- */
	class RequestRealTimeModbusData implements Runnable {
		
		boolean requestRealTimeModbusDataStarted; // ���������̵߳Ľ���
		
		private Socket socket; // �׽���
		private BufferedInputStream buffInputStream;
		private BufferedOutputStream buffOutputStream;
		
		// ���췽��
		public RequestRealTimeModbusData(Socket s, InputStream inPutStream, OutputStream outPutStream) {
			this.socket = s;
			this.buffInputStream = new BufferedInputStream(inPutStream);
			this.buffOutputStream = new BufferedOutputStream(outPutStream);
		}
		
		@Override
		public void run() {
			printInformation(1, "�����߳�->����ʵʱ������ݣ���������ʵʱmodbusdata������ݵ��̳߳ɹ���");
			requestRealTimeModbusDataStarted = true;
			try {
				while (requestRealTimeModbusDataStarted) {
					printInformation(1, "�����߳�->����ʵʱ������ݣ���ʼ�������µ�modbusdata�������...");
					
					// ��Ϣ����Ϊ0x0B ������ֻ��Ҫ����һ����Ϣ���;��У�����Ҫ������������
					String strMsg = "0B";
					byte[] buffSend;
					buffSend = ByteUtil.hexStringToBytes(strMsg); // ��stringת����byte����
					// ������Ϣ��server������
					/*
					this.buffOutputStream.write(buffSend);
					this.buffOutputStream.flush();
					*/
					sendMsg(this.buffOutputStream, buffSend, buffSend.length); //---------------------------------------------------------------------write
					Thread.sleep(5000);// ��ͣһ��ʱ�䣬Ƶ�ʿ�����΢��һ��
				}
			} catch (InterruptedException | IOException e) {
				//e.printStackTrace();
				printInformation(-1, "�����߳�->����ʵʱ������ݣ����棬�����Ѿ��Ͽ���server��������ӣ�����");
			}
		}
	}
	
	/**
	 * ---��дbuffOutputStream.write()������Ϊ��Ӧ��TCP���ӵ�ճ�����⡿
	 * @author CongXiang
	 * ������
	 * 1.BufferedOutputStream buffOutputStream; // �����
	 * 2.byte typeOfMsg; // ��Ϣ����,new String(new byte[] {0x09})
	 * 3.string contentOfMsg; // ���������
	 * 4.int lengthOfMsg; // �����ֽ���
	 * ����ֵ��
	 * 1:�ɹ���������
	 * 0:��������ʧ��
	 * @throws IOException 
	 * */
	public int sendMsg(BufferedOutputStream buffOutputStream, byte[] contentOfMsg, int lengthOfMsg) throws IOException {
		// //String type = new String(new byte[] { typeOfMsg }); //
		// ��byte��Ϣ����ת����string��Ϊ�˷��������string.getbytes();
		// //byte[] buffsend = (type + contentOfMsg).getBytes(); // ���ͻ�����
/*
		buffOutputStream.write(contentOfMsg);// ---------------------------------------------------------------------write
		buffOutputStream.flush();
		printInformation(1, "��������������Ϣ:���͵��������ͣ�" + contentOfMsg[0]);
*/
		
		// 1.���巢�ͻ��������������ĳ���Ϊ��lengthOfMsg + 1 + 1
		byte[] byteSendBuffArray = new byte[lengthOfMsg + 2];
		
		// 2.�����ݻ���������ӡ���Ϣ�峤�ȡ��ֶΣ�1���ֽڣ���16��������ʾ
		byteSendBuffArray[0] = (byte) lengthOfMsg; // ǿ������ת��
		
		// 3.�����ݻ���������ӡ���Ϣ�塱��lengthOfMsg���ֽ�
		for (int i = 0; i < lengthOfMsg; i++) {
			byteSendBuffArray[i+1] = contentOfMsg[i];
		}
		
		// 4.�����ݻ���������ӡ�ֹͣ���š���1���ֽ�
		byteSendBuffArray[lengthOfMsg+1] = (byte) 0xFF;
		
		// 5.���ͻ���������������
		buffOutputStream.write(byteSendBuffArray);// ---------------------------------------------------------------------write
		buffOutputStream.flush();
		printInformation(1, "��������������Ϣ:���͵���������:" + contentOfMsg[0]+",��Ϣ����Ϊ:"+(int)byteSendBuffArray[0]);
		
		return 1;
	}
	
	/**
	 * ---��дbufferInputStream.read()������Ϊ��Ӧ��TCP���ӵ�ճ�����⡿
	 * @author CongXiang
	 * ������
	 * 1.BufferedInputStream bufferInputStream ; // ������
	 * 2.byte[] recvBuff; // ���ջ�����
	 * 3.
	 * ����ֵ��
	 * recvNum; // ���յ��ֽ�����
	 * @throws IOException 
	 * */
	public int recvMsg(BufferedInputStream bufferInputStream, byte[] recvBuff) throws IOException {
/*		
		int num; // ��¼���յ��ֽ�����
		num = bufferInputStream.read(recvBuff);
		return num;
*/
		// 1.����һ���ַ�����Ϣ��ĳ���lengthOfMsg
		byte[] byteTemp = new byte[1]; // һ���ֽڼ���
		int lengthOfMsg;
		bufferInputStream.read(byteTemp, 0, 1);
		lengthOfMsg = (int) byteTemp[0];
		
		// 2.����lengthOfMsg���ַ�
		int recvNum = bufferInputStream.read(recvBuff, 0, lengthOfMsg);
		
		// 3.�ٶ���һ���ַ����ж��ǲ��ǡ�ֹͣ����0xFF��
		bufferInputStream.read(byteTemp, 0, 1);
		
		if((byteTemp[0] == (byte) 0xFF) && (recvNum == lengthOfMsg)){
			// 4.�����ֹͣ���ţ�����recvNum == lengthOfMsg ��˵��������ȷ
			return recvNum;
		}else{
			// 5.�������ֹͣ���ţ�����recvNum != lengthOfMsg����˵�����մ���>>>���մ�����:��ͣ�Ķ�����һ���ַ���֪������0xFFΪֹ
			while(bufferInputStream.read(byteTemp, 0, 1) == 1){
				if(byteTemp[0] == 0xFF){
					break;
				}
			}
			return -1; // ��ʶ�������ݳ���
		}

	}
	
	/**
	 * ---��ӡ�����״̬
	 * 
	 * @author CongXiang ˼·�����ݱ�ʶ���Ĳ�ͬ��ѡ���ǡ�����ֱ̨����������ǡ��ڳ�����������
	 * */
	public void printInformation(int systemOrApplication, String strMsg) {
/*		
 		if (systemOrApplication == 0) { // ϵͳ���
			System.out.println(strMsg);
		} else if (systemOrApplication == 1) { // ����������������
			tfState.setForeground(Color.BLACK);
			tfState.setText(strMsg);
		} else if (systemOrApplication == -1) { // ������Ϣ���\
			tfState.setForeground(Color.RED);
			tfState.setText(strMsg);
		} else {
			System.out.println("�����ʽ�������������ʽ��-1�ǳ�����Ϣ�����0��ϵͳ�����1�ǽ���������");
		}
*/		
		switch(systemOrApplication){

		case 0:// ϵͳ���
			System.out.println(strMsg.trim());
			break;
			
		case 1:// ����������
			tfState.setForeground(Color.BLACK);
			tfState.setText(strMsg);
			break;
			
		case -1:// ������Ϣ���
			tfState.setForeground(Color.RED);
			tfState.setText(strMsg);
			break;
		default:
			System.out.println("�����ʽ�������������ʽ��-1�ǳ�����Ϣ�����0��ϵͳ�����1�ǽ������");
			break;
		}
	}
	
	/**
	 * ---������modbusdataʵʱ������ݵ�byte[]ת����string[]
	 * @author CongXiang
	 * ������
	 * byte[]�������� + ʱ�� + ��ַ���� + modbus�ն˵�IP��ַ + ��λ�豸ID + modbusdata
	 * length��byte[]��ʵ����Ч���ݵĳ���
	 * ����ֵ��
	 * string[]��ʱ�� + modbus�ն˵�IP��ַ + ��λ�豸ID + modbusdata
	 * */
	public static String[] byteModbusDataToStringArray(byte[] byteModbusData, int length) {
		int pos, lengthOfIP;
		String[] strModbusDataMsg = new String[4];
		String str = new String(byteModbusData);

		pos = 1; // ָ��ʱ�俪ʼ
		strModbusDataMsg[0] = str.substring(pos, pos + 19); // ʱ��

		pos = pos + 19; // ָ��IP��ַ���ȵĿ�ʼ
		lengthOfIP = Integer.valueOf(str.substring(pos, pos + 2)); // IP��ַ�ĳ���

		pos = pos + 2; // ָ��IP��ַ�Ŀ�ʼ
		strModbusDataMsg[1] = str.substring(pos, pos + lengthOfIP); // modbus�ն˵�IP��ַ

		pos = pos + lengthOfIP; // ָ����λ�豸ID��ʼ
		strModbusDataMsg[2] = str.substring(pos, pos + 2); // ��λ�豸��ID

		// pos = pos + 2; // ָ��modbusdataʵʱ������ݿ�ʼ
		strModbusDataMsg[3] = modbusDataToRealData(str.substring(pos, length)); // modbusdataʵʱ�������
		
		/** ���ܣ�������ʶ���modbus��Ϣת��������������ʽ  */

		return strModbusDataMsg;

	}
	/**
	 * ---�������豸״̬�ĵ�byte[]ת����string[]
	 * @author CongXiang
	 * ������
	 * byte[]�������� + ʱ�� + ��ַ���� + modbus�ն˵�IP��ַ + ��λ�豸ID + �豸״̬
	 * length��byte[]��ʵ����Ч���ݵĳ���
	 * ����ֵ��
	 * string[]��ʱ�� + modbus�ն˵�IP��ַ + ��λ�豸ID + modbusdata
	 * */
	public static String[] byteDeviceStateToStringArray(byte[] byteModbusData, int length) {
		int pos, lengthOfIP;
		String[] strModbusDataMsg = new String[4];
		String str = new String(byteModbusData);

		pos = 1; // ָ��ʱ�俪ʼ
		strModbusDataMsg[0] = str.substring(pos, pos + 19); // ʱ��

		pos = pos + 19; // ָ��IP��ַ���ȵĿ�ʼ
		lengthOfIP = Integer.valueOf(str.substring(pos, pos + 2)); // IP��ַ�ĳ���

		pos = pos + 2; // ָ��IP��ַ�Ŀ�ʼ
		strModbusDataMsg[1] = str.substring(pos, pos + lengthOfIP); // modbus�ն˵�IP��ַ

		pos = pos + lengthOfIP; // ָ����λ�豸ID��ʼ
		strModbusDataMsg[2] = str.substring(pos, pos + 2); // ��λ�豸��ID

		pos = pos + 2; // ָ��modbusdataʵʱ������ݿ�ʼ
		strModbusDataMsg[3] = str.substring(pos, length); // modbusdataʵʱ�������

		return strModbusDataMsg;

	}
	
	/**
	 * ---��modbusdataװ�����ܹ�ʶ�����ʪ��
	 * @author CongXiang
	 * ������
	 * String modbusData
	 * ����ֵ��
	 * string:�豸��+�����+�¶�+ʪ��
	 * ������01030000000AC5CD
	 * 02  03  00  00  00  04  44  3A
	 * 02 03 08 41 BA 28 F6 42 62 44 B3 18 8A 
	 * 02 03 08 41 BA 28 F6 42 62 44 B3 4D 8A
	 * */
	public static String modbusDataToRealData(String modbusData){
		
		//System.out.println(modbusData);

		// 1.��ȡ�豸��
		String deviceId = modbusData.substring(0,2);
		
		if(deviceId.equals("02")){
			// 2.��ȡ�����
			String modbusOrder = modbusData.substring(2,4);
			
			// 3.��ȡ�¶�
	        Float temp =Float.intBitsToFloat(Integer.valueOf(modbusData.substring(6, 14), 16));
	        
			// 4.��ȡʪ��
	        Float humi =Float.intBitsToFloat(Integer.valueOf(modbusData.substring(14, 22), 16));
	        
	        // 5.��������
	        String str="�豸:"+deviceId + ",�����:" + modbusOrder + ",�¶�:" + temp + ",ʪ��:" + humi + "";
			return str;
		}else{
			return modbusData; // ���ܽ�����modbus����
		}

		
	}
	
	// ���ý��水ť״̬�ķ���
	public  void buttonState(boolean isBtConnectServer, boolean isBtGenerateModbusOrder){
		connectServerPanel.btConnectServer.setEnabled(isBtConnectServer);
		modbusMsgPanel.btGenerate.setEnabled(isBtGenerateModbusOrder);
	}
	// main����
	public static void main(String[] args) {
		new Application();
		//System.out.println(Application.modbusDataToRealData("02030841BA28F6426244B34D8A"));
	}
}
