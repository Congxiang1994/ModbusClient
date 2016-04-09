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
	 * ---���ַ���ת����һ���ڵ������Ա��ں����ڽ�������ʾ��
	 * 
	 * @author CongXiang
	 * 
	 * ע�⣺�ַ����ĸ�ʽ���£� 
	 *         1.��һλ����Ϣ���ͣ�08 
	 *         2.�ڶ�λ��IP�ĳ��ȣ�xx,��ΪIP��ַ�ĳ����ǲ�һ������Ҫָ������
	 *         3.����λ��IP��ַ��xxxx(������ǰһ���ֶ�ָ��) 
	 *         4.����λ����λ�豸�ţ�FF����û����λ�豸��xx(�����ֽ�)
	 *         5.����IP���豸���ں���˳�����ӣ�һ���ͣ�����λ����װ
	 * 
	 *         �����㷨�� 
	 *         1.��Ϊserver���豸��������Ϣ��������ʱ��û�ж��豸��������
	 *         2.���ԣ���Ҫ�����Լ�ȥ�ж��ĸ��Ǹ��ڵ㣬�ĸ����ӽڵ� 
	 *         3.���ճ��Ȳ�ֽڵ㣻
	 *         4.�����modbus�ն˽ڵ㣬����λ�����ӽڵ㣬������û���ظ���
	 *         5.�������λ�豸�ڵ㣬���ж�������modbus�ն˽ڵ��Ƿ��Ѿ����ڣ�Ȼ���������Ӧ����λ�豸�ڵ㵽modsbus�ն˽ڵ���
	 * */

	public static Node stringToNodeTree(String str) {

		// 0814/172.29.143.6700
		int lengthStr = str.length();

		// 1.����һ�����ڵ㣬����ڵ������λ��
		Node nodeRoot = new Node("��λ��");

		// 2.���ճ��ȱ����ַ�����0814/172.29.143.670114/172.29.143.670214/172.29.143.67FF
		int pos = 1;
		String strLengthOfIP;
		String strTreminal;
		String strDeviceID;
		List<Node> listTerminalNodes = null; // modbus�ն˽ڵ���б�
		int numTerminal; // �ж�modbus�ڵ��Ƿ��Ѿ�����

		while (pos < lengthStr) { // һֱѭ�������ף��ַ�����������

			numTerminal = -1;

			// ��ȡIP��ַ�ĳ���
			strLengthOfIP = str.substring(pos, pos + 2);
			//System.out.println("IP��ַ�ĳ���Ϊ��" + strLengthOfIP);

			// ���ַ�����ȡ��IP��ַ
			strTreminal = str.substring(pos + 2, pos + 2 + Integer.valueOf(strLengthOfIP));
			//System.out.println("modbus�ն˵�IP��ַΪ��" + strTreminal);

			// ���ַ����н��豸IDȡ����
			strDeviceID = str.substring(pos + 2 + Integer.valueOf(strLengthOfIP), pos + 2 + Integer.valueOf(strLengthOfIP) + 2);
			//System.out.println("��λ�豸��IDΪ��" + strDeviceID);

			// ��modbus�ն˽ڵ���ӵ����ϣ�
			/** 1.�ȱ���һ��modbus�ն˵��б��ж�ʱ���Ѿ��������modbus�նˣ����û������� */
			listTerminalNodes = nodeRoot.getChilds();
			if (listTerminalNodes != null) {
				//System.out.println("111");
				for (int i = 0; i < listTerminalNodes.size(); i++) {
					if (listTerminalNodes.get(i).getName().equals(strTreminal)) { // ��ǰ��������ڵ�
						numTerminal = i;
						break;
					}
				}
			}

			Node nodeTreminal = new Node();
			Node nodeDevice = new Node();
			if (numTerminal == -1) {
				// ���modbus�ն˽ڵ�
				//System.out.println("222");
				nodeTreminal.setName(strTreminal);
				nodeRoot.add(nodeTreminal);

				// �жϣ������λ�豸�ڵ�
				if (strDeviceID.equals("FF") == false) { // ������Ч����λ�豸ID
					// �����λ�豸
					nodeDevice.setName(strDeviceID);
					nodeTreminal.add(nodeDevice);
				}

			} else { // ����Ҫ���modbus�ն˽ڵ�
				if (strDeviceID.equals("FF") == false) { // ������Ч����λ�豸ID
					// �����λ�豸
					nodeDevice.setName(strDeviceID);
					listTerminalNodes.get(numTerminal).add(nodeDevice);
				}
			}

			// ���¶�λ��׼��������һ���ڵ�
			pos = pos + 2 + Integer.valueOf(strLengthOfIP) + 2;
		}

		return nodeRoot;
	}

	/**
	 * ��ȫչ��һ��JTree
	 * 
	 * @param tree
	 *            JTree
	 */
	public static void expandTree(JTree tree) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), true);
	}

	/**
	 * ��ȫչ����ر�һ����,���ڵݹ�ִ��
	 * 
	 * @param tree
	 *            JTree
	 * @param parent
	 *            ���ڵ�
	 * @param expand
	 *            Ϊtrue���ʾչ����,����Ϊ�ر�������
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

		// ���豸״̬��ʾ�����νṹͼ��
		DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode("��λ��");
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

		StateTreePanel stateTreePanel = new StateTreePanel(nodeRoot);
		JFrame frame = new JFrame();
		Container panel = frame.getContentPane();
		panel.add(stateTreePanel);
		frame.setSize(300, 250);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		DefaultMutableTreeNode nodeNew = new DefaultMutableTreeNode("hahaha");
		//nodeRoot.removeAllChildren(); // ɾ�������ӽڵ㣬��ɾ�����ڵ�
		nodeRoot.remove(0);// ɾ����һ���ӽڵ㼰���ӽڵ��µ����нڵ�
		nodeRoot.add(nodeNew); // ���һ���ӽڵ�
/*		stateTreePanel.treeModel. = new DefaultTreeModel(nodeRoot);
		stateTreePanel.tree = new JTree(stateTreePanel.treeModel);*/
		stateTreePanel.treeModel.reload(nodeRoot);
		UtilTool.expandTree(stateTreePanel.tree);
	}
	

}
