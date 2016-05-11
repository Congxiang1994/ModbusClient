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
 * 上位机程序
 * 
 * @author CongXiang
 * */

public class Application extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// 变量
	public Socket server;
	public InputStream inPutStream;
	public OutputStream outPutStream;
	public BufferedInputStream buffInputStream;
	public BufferedOutputStream buffOutputStream;
	
	public boolean isConnectServer ;

	public DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode("上位机");

	// 面板对象
	public TreePanel treePanel = new TreePanel();
	public StateTreePanel stateTreePanel = new StateTreePanel(nodeRoot);
	public GraphPanel graphPanel = new GraphPanel();
	public StateMsgPanel stateMsgPanel = new StateMsgPanel();
	public ConnectServerPanel connectServerPanel = new ConnectServerPanel();
	public ModbusMsgPanel modbusMsgPanel = new ModbusMsgPanel();
	public HistoricalDataPanel historicalDataPanel = new HistoricalDataPanel();
	public RealTimeModbusDataPanel realTimeModbusDataPanel = new RealTimeModbusDataPanel();

	// 底部状态栏
	public JTextField tfState = new JTextField("状态栏:");

	// 右键菜单
	public MenuItem menuItem = new MenuItem("删除");

	// 构造方法
	public Application() {
		
		isConnectServer = false; // 尚未连接服务器

		/* 上位机控制界面布局--------------------------------------------------------------------------------------------------- */
/*		
		// 三合一面板
		Panel panelThreeInOne = new Panel();
		GridBagLayout gridBagLayoutThreeInOne = new GridBagLayout();
		panelThreeInOne.setLayout(gridBagLayoutThreeInOne);
		// 设备状态列表
		gridBagLayoutThreeInOne.setConstraints(stateMsgPanel, new GBC(0, 0, 2, 2).setWeight(1, 1).setFill(GBC.BOTH));
		panelThreeInOne.add(stateMsgPanel);
		// 连接服务器
		gridBagLayoutThreeInOne.setConstraints(connectServerPanel, new GBC(0, 2, 2, 1).setWeight(1, 0).setFill(GBC.BOTH));
		panelThreeInOne.add(connectServerPanel);
		// 生成并更新modbus命令
		gridBagLayoutThreeInOne.setConstraints(modbusMsgPanel, new GBC(2, 0, 0, 0).setWeight(1, 1).setFill(GBC.BOTH));
		panelThreeInOne.add(modbusMsgPanel);

		// 四合一面板
		Panel panelFourInOne = new Panel();
		GridLayout gridLayout = new GridLayout(2, 2);
		panelFourInOne.setLayout(gridLayout);

		panelFourInOne.add(stateTreePanel);
		panelFourInOne.add(realTimeModbusDataPanel); // 这里替换成一个modbusdata实时监测数据显示的表格面板
		panelFourInOne.add(panelThreeInOne);
		panelFourInOne.add(historicalDataPanel);
		
	*/	
		// 二合一面板：连接服务器 + 生成modbus命令数据
		Panel panelConnectAndOrder = new Panel();
		GridBagLayout gridBagLayoutThreeInOne = new GridBagLayout();
		panelConnectAndOrder.setLayout(gridBagLayoutThreeInOne);
		gridBagLayoutThreeInOne.setConstraints(connectServerPanel, new GBC(0, 0, 2, 1).setWeight(1, 0).setFill(GBC.BOTH));// 连接服务器
		panelConnectAndOrder.add(connectServerPanel);
		gridBagLayoutThreeInOne.setConstraints(modbusMsgPanel, new GBC(0, 2, 2, 2).setWeight(1, 1).setFill(GBC.BOTH));// 设备状态列表
		panelConnectAndOrder.add(modbusMsgPanel);

		
		// 二合一面板：“连接服务器 + 生成modbus命令数据” + 状态树 + 生成modbus数据
		Panel panelThreeInOneAndStateTree = new Panel();
		GridLayout gridLayout = new GridLayout(1, 2);
		panelThreeInOneAndStateTree.setLayout(gridLayout);
		
		panelThreeInOneAndStateTree.add(panelConnectAndOrder);
		panelThreeInOneAndStateTree.add(stateTreePanel);
		//panelThreeInOne.add(stateMsgPanel); 
		
		// 二合一面板：上述面板+实时状态信息
		Panel panelThreeInOneAndRealTimeState = new Panel();
		GridLayout gridLayoutRealTimeState = new GridLayout(1, 2);
		panelThreeInOneAndRealTimeState.setLayout(gridLayoutRealTimeState);
		
		panelThreeInOneAndRealTimeState.add(panelThreeInOneAndStateTree);
		panelThreeInOneAndRealTimeState.add(stateMsgPanel);
		
		// 二合一面板：上述二合一面板 + 实时 modbusdata监测数据面板
		
		Panel panelTwoInOne = new Panel();
		GridLayout gridLayoutTwo = new GridLayout(2, 1);
		panelTwoInOne.setLayout(gridLayoutTwo);

		panelTwoInOne.add(panelThreeInOneAndRealTimeState);
		panelTwoInOne.add(realTimeModbusDataPanel); 
		
		// 添加底部状态信息栏：
		GridBagLayout gridBagLayoutMain = new GridBagLayout();
		this.setLayout(gridBagLayoutMain);

		gridBagLayoutMain.setConstraints(panelTwoInOne, new GBC(0, 0, 8, 8).setWeight(1, 1).setFill(GBC.BOTH));
		this.add(panelTwoInOne);

		tfState.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		gridBagLayoutMain.setConstraints(tfState, new GBC(0, 8, 8, 1).setWeight(1, 0).setFill(GBC.BOTH));
		this.add(tfState);
		/* -------------------------------------------------------------------------------------------------------------------- */
		
		/* 添加消息响应事件------------------------------------------------------------------------------------------------------*/

		// 连接服务器按钮
		connectServerPanel.btConnectServer.addActionListener(this);

		// 生成modbus命令按钮
		modbusMsgPanel.btGenerate.addActionListener(this);

		// 设置删除modbus命令列表中modbus命令的右键菜单
		PopupMenu pop = new PopupMenu();
		// MenuItem menuItem = new MenuItem("删除");
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

		// 更新数据库中modbus命令的按钮
		modbusMsgPanel.btRefresh.addActionListener(this);
		/* -------------------------------------------------------------------------------------------------------------------- */
		// 设置实时modbus监测数据的列宽
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(0).setPreferredWidth(200);
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(1).setPreferredWidth(200);
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(2).setPreferredWidth(200);
		realTimeModbusDataPanel.tableRealTimeModbusData.getColumnModel().getColumn(3).setPreferredWidth(600);
		/* -------------------------------------------------------------------------------------------------------------------- */
		
		/* -------------------------------------------------------------------------------------------------------------------- */
		
		/* 设置窗口的属性------------------------------------------------------------------------------------------------------- */
		this.setTitle("上位机应用程序");
		this.pack();
		this.setSize(1200, 900);
		this.setLocationRelativeTo(null); // 设置界面在屏幕中央显示
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置单击关闭按钮能够关闭主进程
		this.setVisible(true); // 显示窗口
		/* -------------------------------------------------------------------------------------------------------------------- */
		// 设置按钮状态
		this.buttonState(true, false);
	}

	/* 消息相应事件 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectServerPanel.btConnectServer) { /* 请求连接服务器 -----------------------------------------*/
			printInformation(1, "开始连接服务器程序...");
			// 设置按钮状态
			this.buttonState(false, false);
			try {
				// 1.建立socket通信的必要变量
				server = new Socket(connectServerPanel.tfIPAddress.getText().trim(), Integer.valueOf(connectServerPanel.tfPort.getText().trim()));
				inPutStream = server.getInputStream();
				outPutStream = server.getOutputStream();
				buffInputStream = new BufferedInputStream(server.getInputStream());
				buffOutputStream = new BufferedOutputStream(server.getOutputStream());
				printInformation(1, "连接服务器成功。");

				// 2.建立接收数据的线程，并启动该线程 
				ReceiveMsgFromServer receiveMsgFromServer = new ReceiveMsgFromServer(server, buffInputStream, buffOutputStream);
				new Thread(receiveMsgFromServer).start(); // 启动接收数据的线程
				printInformation(1, "启动接收数据的线程。");

				Thread.sleep(1000);// 睡眠一秒，等待server服务器建立相关的接收数据，否则服务器端没法正确接收数据

				// 3.向server发送0x00，标识自己是上位机
				byte[] buffID = new byte[] { 0x00 };
				/*
				buffOutputStream.write(buffID); // 发送一个0x00，表示自己是上位机//---------------------------------------------------------------------write
				buffOutputStream.flush();
				 */
				sendMsg(buffOutputStream, buffID,1);// 发送一个0x00，表示自己是上位机//---------------------------------------------------------------------write
				
				Thread.sleep(500);
				

/*				
				// 4.建立请求实时modbusdata监测数据的线程，并启动该线程
				RequestRealTimeModbusData RequestRealTimeModbusData = new RequestRealTimeModbusData(server, buffInputStream, buffOutputStream);
				new Thread(RequestRealTimeModbusData).start();
*/
				// 5.标识已经成功连接server服务器
				isConnectServer = true;
				
			
				// 6.建立请求设备状态的线程，并启动该线程
				RequestEquipmentState requestEquipmentState = new RequestEquipmentState(server, buffInputStream, buffOutputStream);
				new Thread(requestEquipmentState).start();
				// 设置按钮状态
				this.buttonState(false, true);
				
				
			} catch (NumberFormatException | IOException | InterruptedException e1) {
				printInformation(-1, "警告：连接服务器出现错误，请检查网络连接及连接参数！！！");
				// 设置按钮状态
				this.buttonState(true, false);
				// e1.printStackTrace();
			}
			/* -------------------------------------------------------------------------------------------------------------------- */
		} else if (e.getSource() == modbusMsgPanel.btGenerate) { /* 生成modbus命令 --------------------------------------------------*/
			
			// 1.生成modbusorder(不包含CRC校验部分)；01030000000AC5CD
			String strModbusMsg = "";
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfDevice.getText().substring(0, 2).trim(); // 设备号
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfOrderNumber.getText().substring(0, 2).trim(); // 功能码
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfAddressHigh.getText().substring(0, 2).trim(); // 寄存器地址高8位
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfAddressLow.getText().substring(0, 2).trim(); // 寄存器地址低8位
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfCountHigh.getText().substring(0, 2).trim(); // 寄存器数量高8位
			strModbusMsg = strModbusMsg + modbusMsgPanel.tfCountLow.getText().substring(0, 2).trim(); // 寄存器数量的低8位

			// 2.计算CRC16校验码，注意CRC校验低8位在前，高8位在后
			int crc = CRC16.calcCrc16(ByteUtil.hexStringToBytes(strModbusMsg));
			String strCRC = String.format("%04x", crc).toUpperCase();
			//System.out.println(strCRC);
			strModbusMsg = strModbusMsg + strCRC.substring(2, 4); // CRC校验低8位
			strModbusMsg = strModbusMsg + strCRC.substring(0, 2); // CRC校验高8位

			// 3.遍历整个表格，
			boolean isModbusMsgExist = false;
			for (int i = 0; i < modbusMsgPanel.tableModel.getRowCount(); i++) {
				if (strModbusMsg.equals(modbusMsgPanel.tableModel.getValueAt(i, 0))) {
					printInformation(-1, "警告：该modbus命令已经出现过了！！！");
					isModbusMsgExist = true;
					break; // 下面的代码不需要执行
				}
			}
			
			// 4.将新生成的modbus命令放到下方列表中,！！！！！！！！>>>>>>>>>>>这里需要判断是否连接数据库，如果没有的话，则不能添加modbus命令
			if (isModbusMsgExist == false && isConnectServer == true) {
				String strTerminalIP;
				String str[] = new String[2];
				str[0] = strModbusMsg;
				strTerminalIP = "/"+modbusMsgPanel.tfModbusTerminalIp.getText().trim();
				str[1] = strTerminalIP;
				modbusMsgPanel.tableModel.addRow(str);
				//modbusMsgPanel.tableModbusOrderList.setRowSelectionInterval(modbusMsgPanel.tableModel.getRowCount()-1, modbusMsgPanel.tableModel.getRowCount()-1);;
				
				/* 让滚轮自动滚到最后一行 */
				int rowCount = modbusMsgPanel.tableModbusOrderList.getRowCount();
				modbusMsgPanel.tableModbusOrderList.getSelectionModel().setSelectionInterval(rowCount-1, rowCount-1);
				Rectangle rect = modbusMsgPanel.tableModbusOrderList.getCellRect(rowCount-1, 0, true);
				//modbusMsgPanel.tableModbusOrderList.repaint(); //若需要的话
				modbusMsgPanel.tableModbusOrderList.updateUI();//若需要的话
				modbusMsgPanel.tableModbusOrderList.scrollRectToVisible(rect);
				
				printInformation(1, "添加modbus命令成功。");
				/**
				 * >>>存在的问题： 1.不能将新插入的数据放在第一行 2.不能够识别非法字符
				 * */
				// 4-5.将modbus终端的IP地址添加到modbus消息中
				strModbusMsg = strModbusMsg + ByteUtil.intToString(strTerminalIP.length()) + strTerminalIP;
				
				// 5.组装添加modbus命令的消息类型0x0F,消息内容：0x0F+modbus命令
				String strInsertModbusOrder = new String(new byte[] {0x0F}) + strModbusMsg; 
				byte[] buffSendInsertModbusOrder = strInsertModbusOrder.getBytes();
				// 6.将消息发送给Server程序
				try {
					sendMsg(buffOutputStream, buffSendInsertModbusOrder, buffSendInsertModbusOrder.length);// ---------------------------------------------------------------------write
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}else{
				printInformation(-1, "警告：尚未连接server服务器程序或者已经存在这条modbus命令，不能添加modbus命令！！！");
			}
			/* -------------------------------------------------------------------------------------------------------------------- */
		} else if (e.getSource() == modbusMsgPanel.btRefresh) { /* 更新modbus命令到数据库中 ----------------------------------------- */
			
			// 1.先判断modbus命令列表中是否有数据,是否已经成功连接服务器
			if ((modbusMsgPanel.tableModel.getRowCount() > 0) && isConnectServer == true) {
				
				// 2.将modbus列表中的数据存进一个string数组，String[] str3= (String[])vec.get(0);
				Object[] objModbus = modbusMsgPanel.tableModel.getDataVector().toArray();
				String[] strModbus = new String[objModbus.length];
				for (int i = 0; i < objModbus.length; i++) {
					strModbus[i] = objModbus[i].toString().substring(1, 17); // modbus命令长为16，截取的原因：去掉前后中括号，[01030000110AC99D]
				}
				printInformation(1, Arrays.toString(strModbus));
				
				// 3.组装需要发送的modbusorder，【所有modbusorder组装在一起发送】
				String strMsg = "06"; // 消息类型
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
					// 4.将组装好的modbusorder发送给server服务器程序
					sendMsg(buffOutputStream, buffSend, buffSend.length);// ---------------------------------------------------------------------write
					
					// printInformation(1, "发送modbus命令给server服务器程序成功。");
				} catch (IOException e1) {
					printInformation(-1, "警告：发送modbus命令给server出错，请检查以太网连接！！！");
				}
			} else if (modbusMsgPanel.tableModel.getRowCount() <= 0) {
				printInformation(-1, "警告：modbus命令列表中没有数据！！！");
			} else if(isConnectServer == false){
				printInformation(-1, "警告：尚未连接server服务器程序！！！");
			}
			/* -------------------------------------------------------------------------------------------------------------------- */
		} else if (e.getSource() == menuItem) { /* 右键菜单，modbus命令列表中的右键删除功能 ------------------------------------------ */
			
			String strTerminalIp;
			
			// 1.获取选中的索引号
			int selectedRow = modbusMsgPanel.tableModbusOrderList.getSelectedRow(); // 获得选中行索引
			printInformation(1, "删除第" + selectedRow + "行的modbus命令");			
			
			// 2.组装添加modbus命令的消息类型0x10,消息内容：0x10+modbus命令
			String strDeleteModbusOrder = new String(new byte[] {0x10}) + modbusMsgPanel.tableModel.getValueAt(selectedRow, 0); 
			strTerminalIp = (String) modbusMsgPanel.tableModel.getValueAt(selectedRow, 1);
			strDeleteModbusOrder = strDeleteModbusOrder + ByteUtil.intToString(strTerminalIp.length()) + strTerminalIp;
			byte[] buffSendDeleteModbusOrder = strDeleteModbusOrder.getBytes();

			// 3.将消息发送给Server程序
			try {
				sendMsg(buffOutputStream, buffSendDeleteModbusOrder, buffSendDeleteModbusOrder.length);// ---------------------------------------------------------------------write
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// 4.删除选中行
			modbusMsgPanel.tableModel.removeRow(selectedRow);
		}
	}



	/**
	 * 处理从server接收的所有数据
	 * 
	 * @author CongXiang 思路： 1.所有消息接收都在这里面处理！！！
	 * */
	class ReceiveMsgFromServer implements Runnable {
		boolean receiveMsgFromServerStarted;
		private Socket socket; // 套接字
		private BufferedInputStream buffInputStream;
		private BufferedOutputStream buffOutputStream;

		// 构造方法
		public ReceiveMsgFromServer(Socket s, InputStream inPutStream, OutputStream outPutStream) {
			this.socket = s;
			this.buffInputStream = new BufferedInputStream(inPutStream);
			this.buffOutputStream = new BufferedOutputStream(outPutStream);
		}

		@Override
		public void run() {
			printInformation(1, "开始进入接收数据线程...");
			receiveMsgFromServerStarted = true;
			
			while (receiveMsgFromServerStarted) {
				// printInformation(1, "进入循环，开始接收消息...");
				byte[] buffRecv = new byte[10000];// 接收第一个字节的缓冲区
				try {
					/*
					int numRecv = this.buffInputStream.read(buffRecv);// ---------------------------------------------------------------------read
					*/
					int numRecv = recvMsg(this.buffInputStream, buffRecv); // ---------------------------------------------------------------------read
					
					// System.out.println("接收的字符数量为："+numRecv);
					// 判断从server服务器端接受的数据是不是为空
					if (numRecv < 0) {
						buttonState(true, false);
						printInformation(-1, "警告：接收消息出错！！！");
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

					// 这里将根据firstBuffer的类型来进行不同的数据处理
					//printInformation(1, "接下来，根据接收的字符判断消息类型..." + buffRecv[0]);
					switch (buffRecv[0]) {
					
					case 0x00:
						printInformation(1, "消息类型：0x00:连接服务器成功，并成功发送0x00给服务器，标识了自己身份。");
						break;
						
					case 0x06:
						printInformation(1, "消息类型：0x06:发送modbus命令给server服务器程序成功。");
						break;
						
					case 0x08:
						printInformation(1, "消息类型：0x08:接收到server返回的设备实时状态消息");

						/* 对接收的设备实时状态消息进行处理 */
						byte[] buffRecvReal = new byte[numRecv];
						for (int i = 0; i < numRecv; i++) {
							buffRecvReal[i] = buffRecv[i];
						}
						String strRecvReal = new String(buffRecvReal); // 如果是0x08消息类型
						Node node = UtilTool.stringToNodeTree(strRecvReal);

						// 将设备状态显示到树形结构图中
						nodeRoot.removeAllChildren(); // 先删除根节点的所有子节点
						if (node.hasChild()) { // 如果有modbus终端
							for (int i = 0; i < node.getChilds().size(); i++) { // 遍历modbus终端列表
								DefaultMutableTreeNode nodeModbus = new DefaultMutableTreeNode(node.getChilds().get(i).getName());
								nodeRoot.add(nodeModbus);
								if (node.getChilds().get(i).hasChild()) {
									for (int j = 0; j < node.getChilds().get(i).getChilds().size(); j++) { // 遍历下位设备列表
										DefaultMutableTreeNode nodeDevice = new DefaultMutableTreeNode(node.getChilds().get(i).getChilds().get(j).getName());
										nodeModbus.add(nodeDevice);
									}
								}
							}
						}
						stateTreePanel.treeModel.reload(nodeRoot); // 刷新树形结构图
						UtilTool.expandTree(stateTreePanel.tree); // 展开所有树形结构图中的节点
						//printInformation(1, "消息类型：0x08:刷新树形结构图成功");
						byte[] buff08 = new byte[] { 0x08 };
						sendMsg(buffOutputStream, buff08, 1);//---------------------------------------------------------------------write
						break;
						
					case 0x0B:
						printInformation(1, "消息类型：0x0B:接收到server返回的设备实时modbusdata监测数据");
						String[] strModbusDataMsg = new String[4]; // 用来存放下面4个变量
						if(numRecv > 1){
							// 1.在工具类中写一个方法，返回一个数组：时间 + modbus终端IP地址 + 设备ID + modbusdata监测数据
							strModbusDataMsg = byteModbusDataToStringArray(buffRecv, numRecv);
							printInformation(1, "消息类型：0x0B:接收到modbusdata监测数据:"+strModbusDataMsg[0]+","+strModbusDataMsg[1]+","+strModbusDataMsg[2]+","+strModbusDataMsg[3]);
							
							// 2.将modbusdata监测数据加进modbusdata实时监测数据列表中
							realTimeModbusDataPanel.tableModel.addRow(strModbusDataMsg);
							
						}
						byte[] buff0B = new byte[] { 0x0B };
						sendMsg(buffOutputStream, buff0B, 1);//---------------------------------------------------------------------write

						break;
						
					case 0x0C:
						printInformation(1, "消息类型：0x0C:接收到server返回的单条设备实时状态信息");
						String[] strModbusStateData = new String[4]; // 用来存放下面4个变量
						if(numRecv > 1){
							// 1.在工具类中写一个方法，返回一个数组：时间 + modbus终端IP地址 + 设备ID + 设备状态
							strModbusStateData = byteDeviceStateToStringArray(buffRecv, numRecv);
							printInformation(1, "消息类型：0x0C:接收到单条设备状态消息:"+strModbusStateData[0]+","+strModbusStateData[1]+","+strModbusStateData[2]+","+strModbusStateData[3]);
							// 2.将modbusdata监测数据加进modbusdata实时监测数据列表中
							stateMsgPanel.tableModel.addRow(strModbusStateData);
							
						}
						
						break;
						
					case 0x0D:
						printInformation(1, "消息类型：0x0D:接收到server返回的设备实时状态树");
						/* 对接收的设备实时状态消息进行处理 */
						byte[] buffRecvFirst = new byte[numRecv];
						for (int i = 0; i < numRecv; i++) {
							buffRecvFirst[i] = buffRecv[i];
						}
						String strRecvFirst = new String(buffRecvFirst); // 如果是0x08消息类型
						Node nodeFirst = UtilTool.stringToNodeTree(strRecvFirst);

						// 将设备状态显示到树形结构图中
						nodeRoot.removeAllChildren(); // 先删除根节点的所有子节点
						if (nodeFirst.hasChild()) { // 如果有modbus终端
							for (int i = 0; i < nodeFirst.getChilds().size(); i++) { // 遍历modbus终端列表
								DefaultMutableTreeNode nodeModbus = new DefaultMutableTreeNode(nodeFirst.getChilds().get(i).getName());
								nodeRoot.add(nodeModbus);
								if (nodeFirst.getChilds().get(i).hasChild()) {
									for (int j = 0; j < nodeFirst.getChilds().get(i).getChilds().size(); j++) { // 遍历下位设备列表
										DefaultMutableTreeNode nodeDevice = new DefaultMutableTreeNode(nodeFirst.getChilds().get(i).getChilds().get(j).getName());
										nodeModbus.add(nodeDevice);
									}
								}
							}
						}
						stateTreePanel.treeModel.reload(nodeRoot); // 刷新树形结构图
						UtilTool.expandTree(stateTreePanel.tree); // 展开所有树形结构图中的节点
						//printInformation(1, "消息类型：0x08:刷新树形结构图成功");
						break;
						
					case 0x0E:
						printInformation(1, "消息类型：0x0E:接收到server返回的modbus命令");
						String strBuffRecv = new String(buffRecv);
						// 1.解析接收到的数据
						String strModbusOrder = strBuffRecv.substring(1, 17); // 直接将字节数组按照ascll码的方式转换成string类型
						//System.out.println(strModbusOrder);
						
						// 2.遍历表格，判断列表中是否已经存在这条modbus命令
						boolean isModbusMsgExist = false;
						for (int i = 0; i < modbusMsgPanel.tableModel.getRowCount(); i++) {
							if (strModbusOrder.equals(modbusMsgPanel.tableModel.getValueAt(i, 0))) {
								printInformation(-1, "警告：该modbus命令已经出现过了！！！");
								isModbusMsgExist = true;
							}
						}
						
						// 3.向modbus命令列表中添加一条数据
						if (isModbusMsgExist == false) {
							String str[] = new String[2];
							str[0] = strModbusOrder;
							int lengthOfTerminalIp = Integer.valueOf(strBuffRecv.substring(17,19));
							str[1] = strBuffRecv.substring(19, 19+lengthOfTerminalIp);
							modbusMsgPanel.tableModel.addRow(str);
							printInformation(1, "添加modbus命令成功。");
							
							
						}
						
						// 4.将消息类型返回给Server程序，（消息从哪里来，就到哪里结束）
						byte[] buff0E = new byte[] { 0x0E };
						sendMsg(buffOutputStream, buff0E, 1);//---------------------------------------------------------------------write
						break;
						
					case 0x0F:
						printInformation(1, "消息类型：0x0F:上位机添加modbus命令成功。");
						break;
						
					case 0x10:
						printInformation(1, "消息类型：0x10:上位机删除modbus命令成功。");
						break;
						
					default:
						printInformation(-1, "警告，接收到未知消息类型！！！");
						break;
					}
				} catch (IOException e) {
					// 设置按钮状态
					buttonState(true, false);
					printInformation(-1, "警告，可能已经断开与server程序的连接！！！");
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

	
	/**向server服务器程序请求设备的状态：0x0D  辅助线程------------------------------------------------------------------------------------------------ */
	/** -------------------------------------------------------------------------------------------------------------------------------------------- */
	class RequestEquipmentState implements Runnable {
		//boolean requestEquipmentStateStarted;
		private Socket socket; // 套接字
		private BufferedInputStream buffInputStream;
		private BufferedOutputStream buffOutputStream;

		// 构造方法
		public RequestEquipmentState(Socket s, InputStream inPutStream, OutputStream outPutStream) {
			this.socket = s;
			this.buffInputStream = new BufferedInputStream(inPutStream);
			this.buffOutputStream = new BufferedOutputStream(outPutStream);
		}

		@Override
		public void run() {
			//requestEquipmentStateStarted = true;
			printInformation(1, "辅助线程->请求设备状态：建立请求设备状态的线程成功。");
			try {
				//while (requestEquipmentStateStarted) {
					printInformation(1, "辅助线程->请求设备状态：开始请求各级设备的状态...");

					// 消息类型为0x0D ，这里只需要发送一个消息类型就行，不需要附带其他数据
					String strMsg = "0D";
					byte[] buffSend;
					buffSend = ByteUtil.hexStringToBytes(strMsg); // 将string转换成byte数组
					// 发送消息给server服务器
					/*
					this.buffOutputStream.write(buffSend);
					this.buffOutputStream.flush();
					*/
					sendMsg(this.buffOutputStream, buffSend, buffSend.length); //---------------------------------------------------------------------write

					Thread.sleep(10000); // 暂停几秒钟，不需要一直请求状态
				//}
			} catch (InterruptedException | IOException e) {
				printInformation(-1, "辅助线程->请求设备状态：警告，可能已经断开与server程序的连接！！！");
				//e.printStackTrace();
			}
		}
	}
	
	/**向server服务器程序请求实时数据：0x0B  辅助线程------------------------------------------------------------------------------------------------ */
	/** ------------------------------------------------------------------------------------------------------------------------------------------- */
	class RequestRealTimeModbusData implements Runnable {
		
		boolean requestRealTimeModbusDataStarted; // 用来控制线程的结束
		
		private Socket socket; // 套接字
		private BufferedInputStream buffInputStream;
		private BufferedOutputStream buffOutputStream;
		
		// 构造方法
		public RequestRealTimeModbusData(Socket s, InputStream inPutStream, OutputStream outPutStream) {
			this.socket = s;
			this.buffInputStream = new BufferedInputStream(inPutStream);
			this.buffOutputStream = new BufferedOutputStream(outPutStream);
		}
		
		@Override
		public void run() {
			printInformation(1, "辅助线程->请求实时监测数据：建立请求实时modbusdata监测数据的线程成功。");
			requestRealTimeModbusDataStarted = true;
			try {
				while (requestRealTimeModbusDataStarted) {
					printInformation(1, "辅助线程->请求实时监测数据：开始请求最新的modbusdata监测数据...");
					
					// 消息类型为0x0B ，这里只需要发送一个消息类型就行，不需要附带其他数据
					String strMsg = "0B";
					byte[] buffSend;
					buffSend = ByteUtil.hexStringToBytes(strMsg); // 将string转换成byte数组
					// 发送消息给server服务器
					/*
					this.buffOutputStream.write(buffSend);
					this.buffOutputStream.flush();
					*/
					sendMsg(this.buffOutputStream, buffSend, buffSend.length); //---------------------------------------------------------------------write
					Thread.sleep(5000);// 暂停一段时间，频率可以稍微慢一点
				}
			} catch (InterruptedException | IOException e) {
				//e.printStackTrace();
				printInformation(-1, "辅助线程->请求实时监测数据：警告，可能已经断开与server程序的连接！！！");
			}
		}
	}
	
	/**
	 * ---重写buffOutputStream.write()方法【为了应对TCP连接的粘包问题】
	 * @author CongXiang
	 * 参数：
	 * 1.BufferedOutputStream buffOutputStream; // 输出流
	 * 2.byte typeOfMsg; // 消息类型,new String(new byte[] {0x09})
	 * 3.string contentOfMsg; // 输出缓冲区
	 * 4.int lengthOfMsg; // 发送字节数
	 * 返回值：
	 * 1:成功发送数据
	 * 0:发送数据失败
	 * @throws IOException 
	 * */
	public int sendMsg(BufferedOutputStream buffOutputStream, byte[] contentOfMsg, int lengthOfMsg) throws IOException {
		// //String type = new String(new byte[] { typeOfMsg }); //
		// 将byte消息类型转换成string，为了方便下面的string.getbytes();
		// //byte[] buffsend = (type + contentOfMsg).getBytes(); // 发送缓冲区
/*
		buffOutputStream.write(contentOfMsg);// ---------------------------------------------------------------------write
		buffOutputStream.flush();
		printInformation(1, "【方法】发送消息:发送的数据类型：" + contentOfMsg[0]);
*/
		
		// 1.定义发送缓冲区，缓冲区的长度为：lengthOfMsg + 1 + 1
		byte[] byteSendBuffArray = new byte[lengthOfMsg + 2];
		
		// 2.在数据缓冲区中添加“消息体长度”字段：1个字节，用16进制数表示
		byteSendBuffArray[0] = (byte) lengthOfMsg; // 强制类型转换
		
		// 3.在数据缓冲区中添加“消息体”：lengthOfMsg个字节
		for (int i = 0; i < lengthOfMsg; i++) {
			byteSendBuffArray[i+1] = contentOfMsg[i];
		}
		
		// 4.在数据缓冲区中添加“停止符号”：1个字节
		byteSendBuffArray[lengthOfMsg+1] = (byte) 0xFF;
		
		// 5.发送缓冲区中所有数据
		buffOutputStream.write(byteSendBuffArray);// ---------------------------------------------------------------------write
		buffOutputStream.flush();
		printInformation(1, "【方法】发送消息:发送的数据类型:" + contentOfMsg[0]+",消息长度为:"+(int)byteSendBuffArray[0]);
		
		return 1;
	}
	
	/**
	 * ---重写bufferInputStream.read()方法【为了应对TCP连接的粘包问题】
	 * @author CongXiang
	 * 参数：
	 * 1.BufferedInputStream bufferInputStream ; // 输入流
	 * 2.byte[] recvBuff; // 接收缓冲区
	 * 3.
	 * 返回值：
	 * recvNum; // 接收的字节数量
	 * @throws IOException 
	 * */
	public int recvMsg(BufferedInputStream bufferInputStream, byte[] recvBuff) throws IOException {
/*		
		int num; // 记录接收的字节数量
		num = bufferInputStream.read(recvBuff);
		return num;
*/
		// 1.读入一个字符：消息体的长度lengthOfMsg
		byte[] byteTemp = new byte[1]; // 一个字节即可
		int lengthOfMsg;
		bufferInputStream.read(byteTemp, 0, 1);
		lengthOfMsg = (int) byteTemp[0];
		
		// 2.读入lengthOfMsg个字符
		int recvNum = bufferInputStream.read(recvBuff, 0, lengthOfMsg);
		
		// 3.再读入一个字符，判断是不是“停止符号0xFF”
		bufferInputStream.read(byteTemp, 0, 1);
		
		if((byteTemp[0] == (byte) 0xFF) && (recvNum == lengthOfMsg)){
			// 4.如果是停止符号，并且recvNum == lengthOfMsg 则说明接收正确
			return recvNum;
		}else{
			// 5.如果不是停止符号，或者recvNum != lengthOfMsg，则说明接收错误。>>>接收错误处理:不停的读入下一个字符，知道读到0xFF为止
			while(bufferInputStream.read(byteTemp, 0, 1) == 1){
				if(byteTemp[0] == 0xFF){
					break;
				}
			}
			return -1; // 标识接收数据出错
		}

	}
	
	/**
	 * ---打印程序的状态
	 * 
	 * @author CongXiang 思路：根据标识量的不同，选择是“控制台直接输出”还是“在程序界面输出”
	 * */
	public void printInformation(int systemOrApplication, String strMsg) {
/*		
 		if (systemOrApplication == 0) { // 系统输出
			System.out.println(strMsg);
		} else if (systemOrApplication == 1) { // 程序界面底栏输出输出
			tfState.setForeground(Color.BLACK);
			tfState.setText(strMsg);
		} else if (systemOrApplication == -1) { // 出错消息输出\
			tfState.setForeground(Color.RED);
			tfState.setText(strMsg);
		} else {
			System.out.println("输出方式出错：请检查输出方式，-1是出错消息输出，0是系统输出，1是界面底栏输出");
		}
*/		
		switch(systemOrApplication){

		case 0:// 系统输出
			System.out.println(strMsg.trim());
			break;
			
		case 1:// 程序界面输出
			tfState.setForeground(Color.BLACK);
			tfState.setText(strMsg);
			break;
			
		case -1:// 出错消息输出
			tfState.setForeground(Color.RED);
			tfState.setText(strMsg);
			break;
		default:
			System.out.println("输出方式出错：请检查输出方式，-1是出错消息输出，0是系统输出，1是界面输出");
			break;
		}
	}
	
	/**
	 * ---将包含modbusdata实时监测数据的byte[]转换成string[]
	 * @author CongXiang
	 * 参数：
	 * byte[]：功能码 + 时间 + 地址长度 + modbus终端的IP地址 + 下位设备ID + modbusdata
	 * length：byte[]的实际有效数据的长度
	 * 返回值：
	 * string[]：时间 + modbus终端的IP地址 + 下位设备ID + modbusdata
	 * */
	public static String[] byteModbusDataToStringArray(byte[] byteModbusData, int length) {
		int pos, lengthOfIP;
		String[] strModbusDataMsg = new String[4];
		String str = new String(byteModbusData);

		pos = 1; // 指向时间开始
		strModbusDataMsg[0] = str.substring(pos, pos + 19); // 时间

		pos = pos + 19; // 指向IP地址长度的开始
		lengthOfIP = Integer.valueOf(str.substring(pos, pos + 2)); // IP地址的长度

		pos = pos + 2; // 指向IP地址的开始
		strModbusDataMsg[1] = str.substring(pos, pos + lengthOfIP); // modbus终端的IP地址

		pos = pos + lengthOfIP; // 指向下位设备ID开始
		strModbusDataMsg[2] = str.substring(pos, pos + 2); // 下位设备的ID

		// pos = pos + 2; // 指向modbusdata实时监测数据开始
		strModbusDataMsg[3] = modbusDataToRealData(str.substring(pos, length)); // modbusdata实时监测数据
		
		/** 功能：将可以识别的modbus消息转换成正常描述方式  */

		return strModbusDataMsg;

	}
	/**
	 * ---将包含设备状态的的byte[]转换成string[]
	 * @author CongXiang
	 * 参数：
	 * byte[]：功能码 + 时间 + 地址长度 + modbus终端的IP地址 + 下位设备ID + 设备状态
	 * length：byte[]的实际有效数据的长度
	 * 返回值：
	 * string[]：时间 + modbus终端的IP地址 + 下位设备ID + modbusdata
	 * */
	public static String[] byteDeviceStateToStringArray(byte[] byteModbusData, int length) {
		int pos, lengthOfIP;
		String[] strModbusDataMsg = new String[4];
		String str = new String(byteModbusData);

		pos = 1; // 指向时间开始
		strModbusDataMsg[0] = str.substring(pos, pos + 19); // 时间

		pos = pos + 19; // 指向IP地址长度的开始
		lengthOfIP = Integer.valueOf(str.substring(pos, pos + 2)); // IP地址的长度

		pos = pos + 2; // 指向IP地址的开始
		strModbusDataMsg[1] = str.substring(pos, pos + lengthOfIP); // modbus终端的IP地址

		pos = pos + lengthOfIP; // 指向下位设备ID开始
		strModbusDataMsg[2] = str.substring(pos, pos + 2); // 下位设备的ID

		pos = pos + 2; // 指向modbusdata实时监测数据开始
		strModbusDataMsg[3] = str.substring(pos, length); // modbusdata实时监测数据

		return strModbusDataMsg;

	}
	
	/**
	 * ---将modbusdata装换成能够识别的温湿度
	 * @author CongXiang
	 * 参数：
	 * String modbusData
	 * 返回值：
	 * string:设备号+命令号+温度+湿度
	 * 举例：01030000000AC5CD
	 * 02  03  00  00  00  04  44  3A
	 * 02 03 08 41 BA 28 F6 42 62 44 B3 18 8A 
	 * 02 03 08 41 BA 28 F6 42 62 44 B3 4D 8A
	 * */
	public static String modbusDataToRealData(String modbusData){
		
		//System.out.println(modbusData);

		// 1.提取设备号
		String deviceId = modbusData.substring(0,2);
		
		if(deviceId.equals("02")){
			// 2.提取命令号
			String modbusOrder = modbusData.substring(2,4);
			
			// 3.提取温度
	        Float temp =Float.intBitsToFloat(Integer.valueOf(modbusData.substring(6, 14), 16));
	        
			// 4.提取湿度
	        Float humi =Float.intBitsToFloat(Integer.valueOf(modbusData.substring(14, 22), 16));
	        
	        // 5.返回数据
	        String str="设备:"+deviceId + ",命令号:" + modbusOrder + ",温度:" + temp + ",湿度:" + humi + "";
			return str;
		}else{
			return modbusData; // 不能解析的modbus数据
		}

		
	}
	
	// 设置界面按钮状态的方法
	public  void buttonState(boolean isBtConnectServer, boolean isBtGenerateModbusOrder){
		connectServerPanel.btConnectServer.setEnabled(isBtConnectServer);
		modbusMsgPanel.btGenerate.setEnabled(isBtGenerateModbusOrder);
	}
	// main方法
	public static void main(String[] args) {
		new Application();
		//System.out.println(Application.modbusDataToRealData("02030841BA28F6426244B34D8A"));
	}
}
