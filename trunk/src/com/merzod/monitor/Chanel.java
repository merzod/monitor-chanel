package com.merzod.monitor;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Chanel {
	private Color color;
	public Chanel(Color c) {
		color = c;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public synchronized final ArrayList<Number> getValues() {
		return values;
	}
	public synchronized void setValue(Number value) {
		if (values.size() == maxLength) {
			Number v = values.remove(0);
			if (v != null)
				sum -= v.doubleValue();
		}
		values.add(value);

		if (value != null) {
			Double v = value.doubleValue();
			if(min == null)
				min = v;
			if(max == null)
				max = v;
			sum += v;
			if (v > max)
				max = v;
			else if (v < min)
				min = v;
			avg = sum / values.size();
		}
	}
	public Double getScale() {
		return scale;
	}
	public void setScale(Double scale) {
		this.scale = scale;
	}
	public synchronized Number getLastValue() {
		if (values.size() > 0)
			return values.get(values.size() - 1);
		return null;
	}
	public synchronized Number getFirstValue() {
		if (values.size() > 0)
			return values.get(0);
		return null;
	}
	private ArrayList<Number> values = new ArrayList<Number>();
	private int maxLength = 20;
	private Double max;
	private Double min;
	private Double avg = 0.;
	private Double sum = 0.;
	
	public Double getMax() {
		return max;
	}
	public Double getMin() {
		return min;
	}
	public Double getAvg() {
		return avg;
	}
	public synchronized void setMaxLength(int max) {
		this.maxLength = max;
		if (values.size() > max)
			for (int i = values.size() - max; i > 0; i--)
				values.remove(0);
	}
	public void reset() {
		min = null;
		max = null;
	}
	private Double scale = 1.;
}
