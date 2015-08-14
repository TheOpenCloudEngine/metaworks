package org.metaworks.widget;

/**
 * Created by jangjinyoung on 15. 8. 14..
 */
public class Label {


    public Label(String html) {
        this.html = html;
    }

    public Label() {
        setHtml("No message");
    }

    String html;

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }
}
