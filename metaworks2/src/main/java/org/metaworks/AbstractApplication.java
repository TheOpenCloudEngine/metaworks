package org.metaworks;

import org.metaworks.ui.*;

import java.sql.Connection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public abstract class AbstractApplication implements Application {
	Type table;
	
		public Type getType(){
			return table;
		}
			
		public void setType(Type value){
			table = value;
		}


///////////// business logic catridges... /////////////


	abstract public void newInstance();	
	abstract public void cut();
	abstract public void paste();
	abstract public void delete();
	public void saveSheet(){};
	abstract public javax.swing.JPanel createPanel();
	
}