package org.metaworks.widget;

import java.util.ArrayList;

public abstract class ComboBox {

	public ArrayList items;
		public ArrayList getItems() {
			return items;
		}
		public void setItems(ArrayList items) {
			this.items = items;
		}
		
	public Object selectedItem;
	    public void setSelectedItem(Object obj)
	    {
	        if(selectedItem != null && !selectedItem.equals(obj) || selectedItem == null && obj != null)
	        {
	            selectedItem = obj;
	        }
	    }
	    public Object getSelectedItem()
	    {
	        return selectedItem;
	    }
    
    public ComboBox()
    {
    	items = new ArrayList();
    }

    public ComboBox(Object aobj[])
    {
        items = new ArrayList();
        items.ensureCapacity(aobj.length);
        int i = 0;
        for(int j = aobj.length; i < j; i++)
            items.add(aobj[i]);

        if(getSize() > 0)
            selectedItem = getElementAt(0);
    }

    public ComboBox(ArrayList arrayList)
    {
        items = arrayList;
        if(getSize() > 0)
            selectedItem = getElementAt(0);
    }



    public int getSize()
    {
        return items.size();
    }

    public Object getElementAt(int i)
    {
        if(i >= 0 && i < items.size())
            return items.get(i);
        else
            return null;
    }

    public int getIndexOf(Object obj)
    {
        return items.indexOf(obj);
    }

    public void addElement(Object obj)
    {
        items.add(obj);
        if(items.size() == 1 && selectedItem == null && obj != null)
            setSelectedItem(obj);
    }

    public void insertElementAt(Object obj, int i)
    {
        items.add(i, obj);
    }

    public void removeElementAt(int i)
    {
        if(getElementAt(i) == selectedItem)
            if(i == 0)
                setSelectedItem(getSize() != 1 ? getElementAt(i + 1) : null);
            else
                setSelectedItem(getElementAt(i - 1));
        items.remove(i);
    }

    public void removeElement(Object obj)
    {
        int i = items.indexOf(obj);
        if(i != -1)
            removeElementAt(i);
    }

    public void removeAllElements()
    {
        if(items.size() > 0)
        {
            int i = 0;
            int j = items.size() - 1;
            items.clear();
            selectedItem = null;
        } else
        {
            selectedItem = null;
        }
    }
}