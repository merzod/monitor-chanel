package com.merzod.monitor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GrafPanel extends JPanel implements InputQueue.Listener {
	private static final long serialVersionUID = 1L;
	private Color background = Color.white;
	private Color gridColor = new Color(192, 192, 192);
	private ArrayList<Axle> axis = new ArrayList<Axle>();
	private boolean showGrid = true;
	private int startPosition = 0;
	private final Frame parent;
	private Legend legend;
	private static GrafPanel instance;
	public Color getBackground() {
		return background;
	}
	public void setBackground(Color c) {
		background = c;
	}
	public Color getGridColor() {
		return gridColor;
	}
	public void setGridColor(Color c) {
		gridColor = c;
	}
	public Color getAxisColor() {
		return getHorizontalAxle().getColor();
	}
	public void setAxisColor(Color c) {
		getHorizontalAxle().setColor(c);
		getVerticalAxle().setColor(c);
	}
	public static GrafPanel getInstance(Frame fr) {
		if(instance == null)
			instance = new GrafPanel(fr);
		return instance;
	}
	public void recalculateMaxChanelLength() {
		Dimension dim = GrafPanel.this.getSize();
		Axle axle = getVerticalAxle();
		int step = axle.getStep();
		int axlePos = (int) (dim.width - (dim.width * axle
				.getPosition()));
		InputQueue.getInstance().setMaxChanelLength(axlePos / step);
	}
	private GrafPanel(Frame frame) {
		this.parent = frame;
		legend = new Legend();
		axis.add(new Axle(true, 0.7, 50));
		axis.add(new Axle(false, 0.05, 50));
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				recalculateMaxChanelLength();
			}
		});
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2
						&& e.getButton() == MouseEvent.BUTTON1) {
					new PropertiesDialog(parent);
				}
			}
		});
	}

	public Axle getHorizontalAxle() {
		for (Axle axle : axis)
			if (axle.isHorizontal())
				return axle;
		return null;
	}

	public Axle getVerticalAxle() {
		for (Axle axle : axis)
			if (!axle.isHorizontal())
				return axle;
		return null;
	}

	public void paint(Graphics g) {
		Dimension dim = this.getSize();
		g.setColor(background);
		g.fillRect(0, 0, dim.width, dim.height);
		if (showGrid)
			paintGrid(g, dim);
		paintAxis(g, dim);
		paintInput(g, dim);
		legend.painLegend(g);
	}

	private void paintInput(Graphics g, Dimension dim) {
		Chanel[] items = InputQueue.getInstance().getChanels();
		int chanelId = 0;
		for (Chanel chanel : items) {
			g.setColor(chanel.getColor());
			Number p1 = null;
			int valueId = 0;
			int x2 = 0;
			synchronized (chanel) {
				for (Number p2 : chanel.getValues()) {
					int x1 = startPosition + getHorizontalAxle().getStep()
							* (valueId - 1);
					x2 = startPosition + getHorizontalAxle().getStep()
							* valueId;
					Number cp1 = onChart(p1, dim, chanel);
					Number cp2 = onChart(p2, dim, chanel);
					if (cp1 != null && cp2 != null) {
						g.fillRect(x2 - 2, cp2.intValue() - 2, 4, 4);
						g.drawLine(x1, cp1.intValue(), x2, cp2.intValue());
					} else if (cp2 != null) {
						paintStart(g, x2, cp2.intValue());
					}
					p1 = p2;
					valueId++;
				}
				Number val = chanel.getLastValue();
				if (val != null)
					g.drawString(val.toString(), x2-10, onChart(val, dim, chanel)
							.intValue()-3);
				val = chanel.getFirstValue();
				if (val != null)
					g.drawString(val.toString(), startPosition - 25, onChart(
							val, dim, chanel).intValue());
			}
			chanelId++;
		}
	}
	private Number onChart(final Number num, final Dimension dim, Chanel ch) {
		if (num != null)
			return getHorizontalAxle().getPosition() * dim.height
					- (num.doubleValue() * ch.getScale());
		return null;
	}

	private void paintStart(Graphics g, int x, int y) {
		g.drawOval(x - 3, y - 3, 6, 6);
	}

	private void paintGrid(Graphics g, Dimension dim) {
		for (Axle axle : axis) {
			if (axle.isHorizontal()) {
				int step = axle.getGridStep();
				int axlePos = (int) (dim.height * axle.getPosition());
				int startPos = axlePos - ((int) (axlePos / step)) * step;
				for (int i = startPos; i < dim.height; i += step) {
					g.setColor(gridColor);
					g.drawLine(0, i, dim.width, i);
				}
			} else {
				int step = axle.getGridStep();
				int axlePos = (int) (dim.width * axle.getPosition());
				int startPos = axlePos - ((int) (axlePos / step)) * step;
				for (int i = startPos; i < dim.width; i += step) {
					g.setColor(gridColor);
					g.drawLine(i, 0, i, dim.height);
				}
			}
		}
	}

	private void paintAxis(Graphics g, Dimension dim) {
		for (Axle axle : axis) {
			g.setColor(axle.getColor());
			if (axle.isHorizontal()) {
				int height = (int) (dim.height * axle.getPosition());
				g.drawLine(0, height, dim.width, height);
			} else {
				startPosition = (int) (dim.width * axle.getPosition());
				int width = (int) (dim.width * axle.getPosition());
				g.drawLine(width, 0, width, dim.height);
			}
		}
	}

	@Override
	public void chanelUpdated() {
		repaint();
	}

	class Legend {
		private int x = 10;
		private int y = 10;
		private DecimalFormat f;
		public Legend() {
			f = new DecimalFormat("#.##");
		}

		public void painLegend(Graphics g) {
			int chanelId = 0;
			for (Chanel chanel : InputQueue.getInstance().getChanels()) {
				Number val = chanel.getLastValue();
				StringBuffer str = new StringBuffer("Chanel ");
				str.append(chanelId).append(" = ").append(val);
				str.append(" (").append(chanel.getMin()).append(" | ");
				str.append(f.format(chanel.getAvg())).append(" | ");
				str.append(chanel.getMax()).append(")");
				g.setColor(chanel.getColor());
				g.drawString(str.toString(), x, y + 11 * chanelId);
				chanelId++;
			}
		}
	}
}
