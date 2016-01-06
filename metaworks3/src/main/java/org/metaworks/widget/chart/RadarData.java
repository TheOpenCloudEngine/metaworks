package org.metaworks.widget.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jjy on 2016. 1. 6..
 */
public class RadarData {

    String label;
        public String getLabel() {
            return label;
        }
        public void setLabel(String label) {
            this.label = label;
        }

    String color;
        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }


    List<Double> data = new ArrayList<Double>();
        public List<Double> getData() {
            return data;
        }
        public void setData(List<Double> data) {
            this.data = data;
        }


}
