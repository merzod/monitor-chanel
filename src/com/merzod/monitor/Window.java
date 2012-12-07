package com.merzod.monitor;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	private int width = 500;
	private int height = 300;
	GrafPanel panel;

	public Window() {
		this.setTitle("Monitor");
		panel = GrafPanel.getInstance(this);
		InputQueue.getInstance().addListener(panel);
		this.setContentPane(panel);
		Dimension size = this.getToolkit().getScreenSize();
		this.setBounds(size.width / 2 - width / 2,
				size.height / 2 - height / 2, width, height);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Thread t = new Thread() {
			public void run() {
				int i = 0;
				Number[] in1 = new Number[]{50, 20, 100};
				Number[] in2 = new Number[]{60, 80};
				Number[] in3 = new Number[]{30, 20, 75, 45};
				Number[] in = in1;
				while (true) {
					i++;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (i % 5 == 0) {
						if (in == in1)
							in = in2;
						else if (in == in2)
							in = in3;
						else if (in == in3)
							in = in1;
					}
					InputQueue.getInstance().put(in);
				}
			}
		};
		// t.start();
	}

	public static void main(String[] args) {
		new Window();
	}
}
