package ch.hftm.util;

import java.time.LocalDate;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class TypeHelper {
    public static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        public LocalDate unmarshal(String v) throws Exception {
            return LocalDate.parse(v);
        }
    
        public String marshal(LocalDate v) throws Exception {
            return v.toString();
        }
    }

    private TypeHelper(){
        
    }
}
