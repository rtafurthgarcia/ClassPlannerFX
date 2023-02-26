package ch.hftm.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class SerializationHelper {
    public static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            if (localDate == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(localDate.toString());
            }
        }
    
        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            } else {
                return LocalDate.parse(jsonReader.nextString());
            }
        }
    }

    public static class DateTimeFormatterAdapter extends TypeAdapter<DateTimeFormatter> {
        @Override
        public void write(final JsonWriter jsonWriter, final DateTimeFormatter formatter) throws IOException {
            if (formatter == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(formatter.toString());
            }
        }
    
        @Override
        public DateTimeFormatter read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            } else {
                return DateTimeFormatter.ofPattern(jsonReader.nextString()); //LocalDate.parse(jsonReader.nextString());
            }
        }
    }

    public static class FileAdapter extends TypeAdapter<File> {
        @Override
        public void write(final JsonWriter jsonWriter, final File file) throws IOException {
            if (file == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(file.getAbsolutePath());
            }
        }
    
        @Override
        public File read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            } else {
                return new File(jsonReader.nextString()); //LocalDate.parse(jsonReader.nextString());
            }
        }
    }
}
