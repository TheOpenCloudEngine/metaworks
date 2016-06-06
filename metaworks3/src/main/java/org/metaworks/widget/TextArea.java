package org.metaworks.widget;

/**
 * Created by jjy on 2016. 6. 1..
 */

public class TextArea {

    int cols = 80;
    public int getCols() {
        return cols;
    }
    public void setCols(int i) {
        cols = i;
    }

    int rows = 1;
    public int getRows() {
        return rows;
    }
    public void setRows(int i) {
        rows = i;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String text;
}
