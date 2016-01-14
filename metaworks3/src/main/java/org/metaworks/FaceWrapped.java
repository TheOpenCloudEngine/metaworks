package org.metaworks;

import org.metaworks.annotation.Hidden;

/**
 * Created by jjy on 2016. 1. 13..
 */
public class FaceWrapped {

    Object value;
    @Hidden
        public Object getValue() {
            return value;
        }
        public void setValue(Object value) {
            this.value = value;
        }

    Face face;
        public Face getFace() {
            return face;
        }
        public void setFace(Face face) {
            this.face = face;
        }

    String faceClass;
    @Hidden
        public String getFaceClass() {
            return faceClass;
        }
        public void setFaceClass(String faceClass) {
            this.faceClass = faceClass;
        }

}
