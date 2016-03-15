package java.util.face;

import org.metaworks.Face;
import org.metaworks.component.SelectBox;

import java.util.Date;

/**
 * Created by jjy on 2016. 2. 24..
 */
public class DateFace extends SelectBox implements Face<Date> {
    @Override
    public void setValueToFace(Date value) {
        getOptionNames().add("1");
        getOptionNames().add("2");
        getOptionNames().add("3");

        setOptionValues(getOptionNames());
    }

    @Override
    public Date createValueFromFace() {
        return null;
    }
}
