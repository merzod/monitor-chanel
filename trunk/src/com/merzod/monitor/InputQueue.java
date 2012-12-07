package com.merzod.monitor;

import java.awt.Color;
import java.util.ArrayList;

public class InputQueue {
	private static InputQueue instance;
	private ArrayList<Listener> listeners;
	private Chanel[] chanels;
	private InputQueue(int chanelsAmount) {
		listeners = new ArrayList<Listener>();
		chanels = new Chanel[chanelsAmount];
		for(int i=0; i<chanelsAmount; i++) {
			chanels[i] = new Chanel(getRandomColor());
		}
	}
	public void setMaxChanelLength(int max) {
		for(Chanel ch : chanels)
			ch.setMaxLength(max);
	}
	public Color getRandomColor() {
		float r = (float) Math.random();
		float g = (float) Math.random();
		float b = (float) Math.random();
		return new Color(r, g, b);
	}
	public static InputQueue getInstance() {
		if (instance == null)
			instance = new InputQueue(4);
		return instance;
	}
	public synchronized void put(Number[] input) {
		for (int i = 0; i < chanels.length; i++) {
			Number val = null;
			if (i < input.length)
				val = input[i];
			chanels[i].setValue(val);
		}
		for (Listener item : listeners)
			item.chanelUpdated();
	}
	public synchronized Chanel[] getChanels() {
		return chanels;
	}
	public void addListener(Listener item) {
		listeners.add(item);
	}
	public static interface Listener {
		public void chanelUpdated();
	}
}
