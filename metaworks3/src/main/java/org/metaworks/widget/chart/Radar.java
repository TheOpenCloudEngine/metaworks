package org.metaworks.widget.chart;

import java.util.List;

/**
 * Created by jjy on 2015. 12. 8..
 */
public class Radar {

    List<String> perspectives;
        public List<String> getPerspectives() {
            return perspectives;
        }
        public void setPerspectives(List<String> perspectives) {
            this.perspectives = perspectives;
        }


    List<RadarData> radarData;
        public List<RadarData> getRadarData() {
            return radarData;
        }

        public void setRadarData(List<RadarData> radarData) {
            this.radarData = radarData;
        }





}
