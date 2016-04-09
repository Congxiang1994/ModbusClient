package com.congxiang.modbus.util;

import java.awt.Container;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.congxiang.modbus.ui.StateTreePanel;

public class UtilTool {

	/**
	 * ---将字符串转换成一个节点树，以便于后来在界面上显示树
	 * 
	 * @author CongXiang
	 * 
	 * 注意：字符串的格式如下： 
	 *         1.第一位是消息类型：08 
	 *         2.第二位是IP的长度：xx,因为IP地址的长度是不一定，需要指定长度
	 *         3.第三位是IP地址：xxxx(长度由前一个字段指定) 
	 *         4.第四位是下位设备号，FF代表没有下位设备：xx(两个字节)
	 *         5.所有IP和设备号在后面顺序连接，一起发送，由上位机组装
	 * 
	 *         基本算法： 
	 *         1.因为server将设备的在线消息传过来的时候并没有对设备进行排序
	 *         2.所以，需要我们自己去判断哪个是根节点，哪个是子节点 
	 *         3.按照长度拆分节点；
	 *         4.如果是modbus终端节点，则上位机的子节点，看看有没有重复的
	 *         5.如果是下位设备节点，先判断所属的modbus终端节点是否已经存在，然后再添加相应的下位设备节点到modsbus终端节点上
	 * */

	public static Node stringToNodeTree(String str) {

		// 0814/172.29.143.6700
		int lengthStr = str.length();

		// 1.建议一个根节点，这个节点就是上位机
		Node nodeRoot = new Node("上位机");

		// 2.按照长度遍历字符串，0814/172.29.143.670114/172.29.143.670214/172.29.143.67FF
		int pos = 1;
		String strLengthOfIP;
		String strTreminal;
		String strDeviceID;
		List<Node> listTerminalNodes = null; // modbus终端节点的列表
		int numTerminal; // 判断modbus节点是否已经存在

		while (pos < lengthStr) { // 一直循环到到底，字符串遍历结束

			numTerminal = -1;

			// 获取IP地址的长度
			strLengthOfIP = str.substring(pos, pos + 2);
			//System.out.println("IP地址的长度为：" + strLengthOfIP);

			// 从字符串中取出IP地址
			strTreminal = str.substring(pos + 2, pos + 2 + Integer.valueOf(strLengthOfIP));
			//System.out.println("modbus终端的IP地址为：" + strTreminal);

			// 从字符串中将设备ID取出来
			strDeviceID = str.substring(pos + 2 + Integer.valueOf(strLengthOfIP), pos + 2 + Integer.valueOf(strLengthOfIP) + 2);
			//System.out.println("下位设备的ID为：" + strDeviceID);

			// 将modbus终端节点添加到树上，
			/** 1.先遍历一下modbus终端的列表，判断时候已经出现这个modbus终端，如果没有则添加 */
			listTerminalNodes = nodeRoot.getChilds();
			if (listTerminalNodes != null) {
				//System.out.println("111");
				for (int i = 0; i < listTerminalNodes.size(); i++) {
					if (listTerminalNodes.get(i).getName().equals(strTreminal)) { // 以前存在这个节点
						numTerminal = i;
						break;
					}
				}
			}

			Node nodeTreminal = new Node();
			Node nodeDevice = new Node();
			if (numTerminal == -1) {
				// 添加modbus终端节点
				//System.out.println("222");
				nodeTreminal.setName(strTreminal);
				nodeRoot.add(nodeTreminal);

				// 判断，添加下位设备节点
				if (strDeviceID.equals("FF") == false) { // 这是有效的下位设备ID
					// 添加下位设备
					nodeDevice.setName(strDeviceID);
					nodeTreminal.add(nodeDevice);
				}

			} else { // 不需要添加modbus终端节点
				if (strDeviceID.equals("FF") == false) { // 这是有效的下位设备ID
					// 添加下位设备
					nodeDevice.setName(strDeviceID);
					listTerminalNodes.get(numTerminal).add(nodeDevice);
				}
			}

			// 重新定位，准备分析下一个节点
			pos = pos + 2 + Integer.valueOf(strLengthOfIP) + 2;
		}

		return nodeRoot;
	}

	/**
	 * 完全展开一个JTree
	 * 
	 * @param tree
	 *            JTree
	 */
	public static void expandTree(JTree tree) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), true);
	}

	/**
	 * 完全展开或关闭一个树,用于递规执行
	 * 
	 * @param tree
	 *            JTree
	 * @param parent
	 *            父节点
	 * @param expand
	 *            为true则表示展开树,否则为关闭整棵树
	 */
	private static void expandAll(JTree tree, TreePath parent, boolean expand) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}

		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	public static void main(String[] args) {
		/*UtilTool util = new UtilTool();*/ 
		//0814/172.29.143.670114/172.29.143.670214/172.29.143.67FF14/172.29.143.670314/172.29.143.77FF
		Node node = UtilTool.stringToNodeTree("0814/172.29.143.6700");
		 node.printAllNode(node);
		// System.out.println(node.getAllNodeName(node));

		// 将设备状态显示到树形结构图中
		DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode("上位机");
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

		StateTreePanel stateTreePanel = new StateTreePanel(nodeRoot);
		JFrame frame = new JFrame();
		Container panel = frame.getContentPane();
		panel.add(stateTreePanel);
		frame.setSize(300, 250);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode("hahaha");
		//nodeRoot.removeAllChildren(); // 删除所有子节点，不删除根节点
		nodeRoot.remove(0);// 删除第一个子节点及该子节点下的所有节点
		nodeRoot.add(nodeNew); // 添加一个子节点
/*		stateTreePanel.treeModel. = new DefaultTreeModel(nodeRoot);
		stateTreePanel.tree = new JTree(stateTreePanel.treeModel);*/
		stateTreePanel.treeModel.reload(nodeRoot);
		UtilTool.expandTree(stateTreePanel.tree);
	}
	

}
