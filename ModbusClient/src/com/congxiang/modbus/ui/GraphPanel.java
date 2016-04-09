package com.congxiang.modbus.ui;

/**
 * =============================================================
 * JFreeChart����������JFreeChart����ʵʱ����
 * =============================================================
 * Description:��������ʾ�˶������ߵļ�ʹ�÷���
 * Original Author:xmf created by 2005-03-03
 *
 * Changes:
 * -------------------------------------------------------------
 * �ڴ˴�ע���޸����ڡ��޸ĵ㡢�޸���
 * -------------------------------------------------------------
 */

//����java2d��
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import com.congxiang.modbus.util.GBC;

public class GraphPanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ����ʵʱ���߶���
	private TimeSeries timeseries1;

	// Value�������ʼֵ
	private double lastValue1;
	private double originalValue1;

	@SuppressWarnings("rawtypes")
	static Class class$org$jfree$data$time$Millisecond;
	static Thread thread1;

	//
	@SuppressWarnings("deprecation")
	public GraphPanel() {
		thread1 = new Thread(this);
		originalValue1 = 100D;
		// ����ʱ��ͼ����
		timeseries1 = new TimeSeries("�¶�(��λ:���϶�)", GraphPanel.class$org$jfree$data$time$Millisecond != null ? GraphPanel.class$org$jfree$data$time$Millisecond : (GraphPanel.class$org$jfree$data$time$Millisecond = GraphPanel.getClass("org.jfree.data.time.Millisecond")));
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeseries1);

		// ����jfreechart����
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("RTU�¶�ģ����ʵʱ����ͼ", "Time", "Value", timeseriescollection, true, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		configFont(jfreechart);

		// �趨��ʾ���
		XYPlot xyplot = jfreechart.getXYPlot();

		// �������ݵ�ɼ�
		XYLineAndShapeRenderer xylinerenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylinerenderer.setShapesVisible(true);
		xylinerenderer.setShapesFilled(true);

		Color jeColor = new Color(9, 166, 89); // ��ɫ

		xyplot.setBackgroundPaint(new Color(0, 255, 255));  // �趨ͼ��������ʾ���ֱ���ɫ

		// �������ݵ����ɫ
		xylinerenderer.setSeriesPaint(0, jeColor); // �������ߵ���ɫ
		xylinerenderer.setShape(new java.awt.geom.Ellipse2D.Double(-2, -2, 4, 4)); // �ı���������״

		xyplot.setBackgroundPaint(Color.lightGray); // �������ߵĳ���������ı�����ɫ
		xyplot.setDomainGridlinePaint(Color.white); // ��Ӧ����������ߣ����ŵ�
		xyplot.setRangeGridlinePaint(Color.white); // ��Ӧ����������ߣ����ŵ�

		// �趨������ķ�Χ
		ValueAxis valueaxis = xyplot.getDomainAxis();
		valueaxis.setAutoRange(true);
		valueaxis.setFixedAutoRange(60000D); // 60��=60000����
		// �趨Value�ķ�Χ��������
		valueaxis = xyplot.getRangeAxis();
		valueaxis.setRange(0.0D, 200D);

		// ����ͼ�����
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		
		// ������岼��
		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);
		gridbaglayout.setConstraints(chartpanel, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(chartpanel);

		// ���ñ߿�
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "ʵʱ������ʾ:");
		this.setBorder(border);
		
		// ������Ҫ��Ӳ�����ť
		JPanel jpanel = new JPanel();
		jpanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));// �߾�Ϊ4
	}

	// �̵߳ĺ��ģ�����ѭ������ͣ�ĸ�������
	public void run() {
		while (true) {
			try {
				// ˵�����ڴ˴���Ӿ����ҵ������

				// �����������1������
				double d1 = 2.0D * Math.random();
				lastValue1 = originalValue1 * d1;
				Millisecond millisecond1 = new Millisecond();
				System.out.println("Series1 Now=" + millisecond1.toString());
				timeseries1.add(millisecond1, lastValue1);

				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	// ��ʼ�߳�
	public static void startThread() {
		thread1.start();
	}

	// �˳���ť����Ϣ��Ӧ����
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("EXIT")) {
			thread1.interrupt();
			System.exit(0);
		}
	}

	@SuppressWarnings("rawtypes")
	static Class getClass(String s) {
		Class cls = null;
		try {
			cls = Class.forName(s);
		} catch (ClassNotFoundException cnfe) {
			throw new NoClassDefFoundError(cnfe.getMessage());
		}
		return cls;
	}

	/**
	 * ��������
	 * 
	 * @param chart
	 *            JFreeChart ����
	 */
	private void configFont(JFreeChart chart) {
		// ��������
		Font xfont = new Font("����", Font.PLAIN, 12);// X��
		Font yfont = new Font("����", Font.PLAIN, 12);// Y��
		Font kfont = new Font("����", Font.PLAIN, 12);// �ײ�
		Font titleFont = new Font("����", Font.BOLD, 25); // ͼƬ����
		XYPlot plot = chart.getXYPlot();// ͼ�εĻ��ƽṹ����

		// ͼƬ����
		chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

		// �ײ�
		chart.getLegend().setItemFont(kfont);

		// X ��
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(xfont);// �����
		domainAxis.setTickLabelFont(xfont);// ����ֵ
		domainAxis.setLabelPaint(Color.BLUE); // ������ɫ
		domainAxis.setTickLabelPaint(Color.BLACK); // ������ɫ

		// Y ��
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(yfont);
		rangeAxis.setLabelPaint(Color.BLUE); // ������ɫ
		rangeAxis.setTickLabelPaint(Color.BLACK); // ������ɫ
		rangeAxis.setTickLabelFont(yfont);

	}

	public static void main(String[] args) {

		GraphPanel Panel = new GraphPanel();
		JFrame frame = new JFrame();
		frame.add(Panel);
		frame.setTitle("RTUʵʱ����");
		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		RefineryUtilities.centerFrameOnScreen(frame); // ��������������Ļ�������

		startThread();
	}
}
