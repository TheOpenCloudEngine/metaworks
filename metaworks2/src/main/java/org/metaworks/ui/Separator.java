package org.metaworks.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.*;


/**
 * @author Jinyoung Jang
 */

public class Separator extends JPanel{

	public final static int STYLE_ETCHED = 1;
	public final static int STYLE_NONE = 0;
	
	boolean isVertical = true;
	boolean sized = false;
	boolean parentListening = false;
	int style;

	public Separator(){
		this(true);
	}
	
	public Separator(final boolean isVertical, int style){
		super();
		//this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.isVertical = isVertical;
		this.style = style;
		
/*		if(isVertical){
			setBackground(Color.BLACK);
		}else*/
			setBackground(Color.WHITE);
		
		setMinimumSize(new Dimension(0,0));
		setPreferredSize(new Dimension(3,2));
	}

	public Separator(final boolean isVertical){
		this(isVertical, STYLE_NONE);
	}

	public void setSize(int w, int h){
		Dimension d = getParent().getSize();
			
		int w_=2;
		int h_=2;
		
		if(isVertical){
			h_ = (int)d.getHeight();// - 10;
		}else{
			w_ = w;
		}
				
		if(!sized)
			super.setSize(w_, h_);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if(isVertical){
			Dimension d = /*getParent().*/getSize();

			int x = (d.width / 2)-1;
			setForeground(Color.GRAY);
			g.fillRect(x, 0, 2, d.height);
		}else{
			Dimension d = /*getParent().*/getSize();

			int y = (d.height / 2)-1;
			if(style==STYLE_ETCHED){
				setForeground(Color.BLACK);
				g.fillRect(0, y, d.width, 2);
				setForeground(Color.GRAY);
				g.fillRect(0, y, d.width, 1);
			}else{
				setForeground(Color.GRAY);
				g.fillRect(0, y, d.width, 2);
			}
		}
		
	}
	
	
}

