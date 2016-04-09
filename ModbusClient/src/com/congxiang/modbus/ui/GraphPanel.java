package com.congxiang.modbus.ui;

/**
 * =============================================================
 * JFreeChart开发：利用JFreeChart开发实时曲线
 * =============================================================
 * Description:该例子演示了多条曲线的简单使用方法
 * Original Author:xmf created by 2005-03-03
 *
 * Changes:
 * -------------------------------------------------------------
 * 在此处注明修改日期、修改点、修改人
 * -------------------------------------------------------------
 */

//导入java2d包
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
	// 申明实时曲线对象
	private TimeSeries timeseries1;

	// Value坐标轴初始值
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
		// 创建时序图对象
		timeseries1 = new TimeSeries("温度(单位:摄氏度)", GraphPanel.class$org$jfree$data$time$Millisecond != null ? GraphPanel.class$org$jfree$data$time$Millisecond : (GraphPanel.class$org$jfree$data$time$Millisecond = GraphPanel.getClass("org.jfree.data.time.Millisecond")));
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeseries1);

		// 创建jfreechart对象
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("RTU温度模拟量实时曲线图", "Time", "Value", timeseriescollection, true, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		configFont(jfreechart);

		// 设定显示风格
		XYPlot xyplot = jfreechart.getXYPlot();

		// 设置数据点可见
		XYLineAndShapeRenderer xylinerenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylinerenderer.setShapesVisible(true);
		xylinerenderer.setShapesFilled(true);

		Color jeColor = new Color(9, 166, 89); // 绿色

		xyplot.setBackgroundPaint(new Color(0, 255, 255));  // 设定图表数据显示部分背景色

		// 设置数据点的颜色
		xylinerenderer.setSeriesPaint(0, jeColor); // 设置折线的颜色
		xylinerenderer.setShape(new java.awt.geom.Ellipse2D.Double(-2, -2, 4, 4)); // 改变坐标点的形状

		xyplot.setBackgroundPaint(Color.lightGray); // 绘制曲线的长方形区域的背景颜色
		xyplot.setDomainGridlinePaint(Color.white); // 对应横坐标的虚线，竖着的
		xyplot.setRangeGridlinePaint(Color.white); // 对应纵坐标的虚线，横着的

		// 设定横坐标的范围
		ValueAxis valueaxis = xyplot.getDomainAxis();
		valueaxis.setAutoRange(true);
		valueaxis.setFixedAutoRange(60000D); // 60秒=60000毫秒
		// 设定Value的范围，纵坐标
		valueaxis = xyplot.getRangeAxis();
		valueaxis.setRange(0.0D, 200D);

		// 创建图表面板
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		
		// 设置面板布局
		GridBagLayout gridbaglayout = new GridBagLayout();
		this.setLayout(gridbaglayout);
		gridbaglayout.setConstraints(chartpanel, new GBC(0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 5, 5, 5));
		this.add(chartpanel);

		// 设置边框
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "实时数据显示:");
		this.setBorder(border);
		
		// 根据需要添加操作按钮
		JPanel jpanel = new JPanel();
		jpanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));// 边距为4
	}

	// 线程的核心：无线循环，不停的更新数据
	public void run() {
		while (true) {
			try {
				// 说明：在此处添加具体的业务数据

				// 随机产生曲线1的数据
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

	// 开始线程
	public static void startThread() {
		thread1.start();
	}

	// 退出按钮的消息相应方法
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
	 * 配置字体
	 * 
	 * @param chart
	 *            JFreeChart 对象
	 */
	private void configFont(JFreeChart chart) {
		// 配置字体
		Font xfont = new Font("宋体", Font.PLAIN, 12);// X轴
		Font yfont = new Font("宋体", Font.PLAIN, 12);// Y轴
		Font kfont = new Font("宋体", Font.PLAIN, 12);// 底部
		Font titleFont = new Font("隶书", Font.BOLD, 25); // 图片标题
		XYPlot plot = chart.getXYPlot();// 图形的绘制结构对象

		// 图片标题
		chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

		// 底部
		chart.getLegend().setItemFont(kfont);

		// X 轴
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(xfont);// 轴标题
		domainAxis.setTickLabelFont(xfont);// 轴数值
		domainAxis.setLabelPaint(Color.BLUE); // 字体颜色
		domainAxis.setTickLabelPaint(Color.BLACK); // 坐标颜色

		// Y 轴
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(yfont);
		rangeAxis.setLabelPaint(Color.BLUE); // 字体颜色
		rangeAxis.setTickLabelPaint(Color.BLACK); // 坐标颜色
		rangeAxis.setTickLabelFont(yfont);

	}

	public static void main(String[] args) {

		GraphPanel Panel = new GraphPanel();
		JFrame frame = new JFrame();
		frame.add(Panel);
		frame.setTitle("RTU实时曲线");
		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		RefineryUtilities.centerFrameOnScreen(frame); // 让整个窗体在屏幕中央居中

		startThread();
	}
}
