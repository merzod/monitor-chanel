package com.merzod.monitor;

import java.awt.Color;

public class Axle {
	private final boolean horizontal;
	private double position;
	private Color color = Color.gray;
	private int gridStep;
	private int stepsInGrid = 10;

	public int getStepsInGrid() {
		return stepsInGrid;
	}

	public void setStepsInGrid(int stepsInGrid) {
		this.stepsInGrid = stepsInGrid;
	}

	public int getGridStep() {
		return gridStep;
	}

	public int getStep() {
		return gridStep / stepsInGrid;
	}

	public void setGridStep(int step) {
		this.gridStep = step;
	}

	public Axle(boolean horizontal, double position, int step) {
		this.horizontal = horizontal;
		this.position = position;
		this.gridStep = step;
	}

	public boolean isHorizontal() {
		return horizontal;
	}
	public double getPosition() {
		return position;
	}
	public void setPosition(double position) {
		this.position = position;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}
