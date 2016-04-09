package com.congxiang.modbus.ui;

import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.congxiang.modbus.util.GBC;
import com.congxiang.modbus.util.UtilTool;

public class StateTreePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public DefaultMutableTreeNode rootNode;
	public DefaultTreeModel treeModel ;
	public JTree tree;

	
	// 构造方法
	public StateTreePanel(DefaultMutableTreeNode root) {
		super();
		rootNode = root;
		treeModel = new DefaultTreeModel(rootNode);
		
		tree = new JTree(treeModel);
		JScrollPane scrollpane = new JScrollPane(tree);
		UtilTool.expandTree(tree);
		
		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "树形结构图:");
		this.setBorder(border);
		
		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);
		
		// 
		gridbaglayout.setConstraints(scrollpane, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(scrollpane);
	}
	public static void main(String[] args) {

		StateTreePanel Panel = new StateTreePanel(new DefaultMutableTreeNode("shangweiji"));
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
