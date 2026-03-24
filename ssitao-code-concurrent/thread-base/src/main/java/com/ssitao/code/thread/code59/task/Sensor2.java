package com.ssitao.code.thread.code59.task;

public class Sensor2 implements Runnable {

	private ParkingCounter counter;

	public Sensor2(ParkingCounter counter) {
		this.counter=counter;
	}

	@Override
	public void run() {
		counter.carIn();
		counter.carOut();
		counter.carOut();
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carIn();
		counter.carIn();
	}

}
