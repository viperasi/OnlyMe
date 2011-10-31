package com.xu81.onlyme.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JLabel;

public class TimeView extends Thread{
	
	private JLabel lbTime;
	private DateFormat format;
	private Date now;
	
	public TimeView(JLabel lb){
		this.lbTime = lb;
		format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,Locale.CHINA);
	}
	
	@Override
	public void run(){
		while(true){
			now = new Date();
			String result = format.format(now);
			lbTime.setText(result);
			try {
				sleep(1000*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
