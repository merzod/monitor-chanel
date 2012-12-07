package com.merzod.monitor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PropertiesDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private Box panel;
	private Frame parent;
	private int width = 150;
	private int height = 120;
	public PropertiesDialog(Frame parent) {
		super(parent, true);
		this.parent = parent;
		setPanel();
		this.setContentPane(panel);
		this.setTitle("Preferances");
		Dimension size = this.getToolkit().getScreenSize();
		this.setBounds(size.width / 2 - width / 2,
				size.height / 2 - height / 2, width, height);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	private void setPanel() {
		int changelId = 0;
		panel = Box.createVerticalBox();
		Box chan = Box.createVerticalBox();
		chan.setBorder(BorderFactory.createTitledBorder("Chanels"));
		for (Chanel ch : InputQueue.getInstance().getChanels()) {
			chan.add(createChanelPanel(ch, changelId));
			changelId++;
		}
		panel.add(chan);
		panel.add(createCommonProps());
		panel.add(createAxleProps());
	}
	private JComponent createAxleProps() {
		JLabel gr = new JLabel("Grid step (px)");
		final JSpinner grSpin = new JSpinner();
		grSpin.setValue(GrafPanel.getInstance(null).getHorizontalAxle()
				.getGridStep());
		grSpin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int val = new Integer(grSpin.getValue().toString());
				GrafPanel.getInstance(null).getHorizontalAxle()
						.setGridStep(val);
				GrafPanel.getInstance(null).getVerticalAxle().setGridStep(val);
				GrafPanel.getInstance(null).recalculateMaxChanelLength();
				PropertiesDialog.this.repaint();
			}
		});
		JLabel st = new JLabel("Steps in grid");
		final JSpinner stSpin = new JSpinner();
		stSpin.setValue(GrafPanel.getInstance(null).getHorizontalAxle()
				.getStepsInGrid());
		stSpin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int val = new Integer(stSpin.getValue().toString());
				GrafPanel.getInstance(null).getHorizontalAxle().setStepsInGrid(
						val);
				GrafPanel.getInstance(null).getVerticalAxle().setStepsInGrid(
						val);
				GrafPanel.getInstance(null).recalculateMaxChanelLength();
				PropertiesDialog.this.repaint();
			}
		});
		JLabel x = new JLabel("X-Axle");
		JLabel y = new JLabel("Y-Axle");
		final JSlider xSlid = new JSlider(0, 100, (int) (GrafPanel.getInstance(
				null).getHorizontalAxle().getPosition() * 100));
		xSlid.setMajorTickSpacing(10);
		xSlid.setPaintTicks(true);
		xSlid.setBackground(panel.getBackground());
		xSlid.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				GrafPanel.getInstance(null).getHorizontalAxle().setPosition(
						xSlid.getValue() / 100.);
				PropertiesDialog.this.repaint();
			}
		});
		final JSlider ySlid = new JSlider(0, 100, (int) (GrafPanel.getInstance(null)
				.getVerticalAxle().getPosition() * 100));
		ySlid.setMajorTickSpacing(10);
		ySlid.setPaintTicks(true);
		ySlid.setBackground(panel.getBackground());
		ySlid.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				GrafPanel.getInstance(null).getVerticalAxle().setPosition(
						ySlid.getValue() / 100.);
				PropertiesDialog.this.repaint();
			}
		});

		JPanel col = new JPanel();
		col.setBackground(panel.getBackground());
		GridLayout lay = new GridLayout(4, 2);
		lay.setHgap(5);
		col.setLayout(lay);
		col.add(gr);
		col.add(grSpin);
		col.add(st);
		col.add(stSpin);
		col.add(x);
		col.add(y);
		col.add(xSlid);
		col.add(ySlid);
		col.setBorder(BorderFactory.createTitledBorder("Axle"));
		return col;
	}
	private JComponent createCommonProps() {
		final JButton back = new JButton("Back");
		back.setBackground(GrafPanel.getInstance(null).getBackground());
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col = JColorChooser.showDialog(PropertiesDialog.this,
						"Choose color", back.getBackground());
				if (col != null) {
					GrafPanel.getInstance(null).setBackground(col);
					back.setBackground(col);
					PropertiesDialog.this.repaint();
				}
			}
		});
		final JButton grid = new JButton("Grid");
		grid.setBackground(GrafPanel.getInstance(null).getGridColor());
		grid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col = JColorChooser.showDialog(PropertiesDialog.this,
						"Choose color", back.getBackground());
				if (col != null) {
					GrafPanel.getInstance(null).setGridColor(col);
					grid.setBackground(col);
					PropertiesDialog.this.repaint();
				}
			}
		});
		final JButton axle = new JButton("Axle");
		axle.setBackground(GrafPanel.getInstance(null).getAxisColor());
		axle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col = JColorChooser.showDialog(PropertiesDialog.this,
						"Choose color", back.getBackground());
				if (col != null) {
					GrafPanel.getInstance(null).setAxisColor(col);
					axle.setBackground(col);
					PropertiesDialog.this.repaint();
				}
			}
		});

		JPanel col = new JPanel();
		col.setBackground(panel.getBackground());
		GridLayout lay = new GridLayout(1, 3);
		lay.setHgap(5);
		col.setLayout(lay);
		col.add(back);
		col.add(grid);
		col.add(axle);
		col.setBorder(BorderFactory.createTitledBorder("UI"));
		return col;
	}
	private JComponent createChanelPanel(final Chanel ch, int i) {
		final JLabel name = new JLabel("Chanel " + i);
		name.setForeground(ch.getColor());
		name.setToolTipText("Click to change color");
		name.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color col = JColorChooser.showDialog(PropertiesDialog.this,
						"Choose color", ch.getColor());
				if (col != null) {
					ch.setColor(col);
					name.setForeground(col);
					PropertiesDialog.this.repaint();
				}
			}
		});
		JSpinner spin = new JSpinner();
		final SpinnerModel model = new SpinModel(ch.getScale());
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ch.setScale((Double) model.getValue());
			}
		});
		spin.setModel(model);
		spin.setPreferredSize(new Dimension(50, 15));
		JButton reset = new JButton("Reset");
		reset.getInsets().set(0, 0, 0, 0);
		reset.setToolTipText("Reset calculated values (Min, Max)");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ch.reset();
			}
		});

		JPanel p = new JPanel();
		p.setBackground(panel.getBackground());
		GridLayout lay = new GridLayout(1, 3);
		lay.setHgap(5);
		p.setLayout(lay);
		p.add(name);
		p.add(spin);
		p.add(reset);

		return p;
	}
	public void repaint() {
		super.repaint();
		parent.repaint();
	}

	class SpinModel implements SpinnerModel {
		private Double value;
		private Double step = 0.1;
		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

		public SpinModel(Double scale) {
			value = scale;
		}

		@Override
		public void addChangeListener(ChangeListener l) {
			listeners.add(l);
		}

		@Override
		public Object getNextValue() {
			return value + step;
		}

		@Override
		public Object getPreviousValue() {
			if ((value - step) <= 0)
				return value;
			return value - step;
		}

		@Override
		public Object getValue() {
			return value;
		}

		@Override
		public void removeChangeListener(ChangeListener l) {
			listeners.remove(l);
		}

		@Override
		public void setValue(Object value) {
			this.value = (Double) value;
			for (ChangeListener l : listeners)
				l.stateChanged(new ChangeEvent(this));
		}
	}
}
