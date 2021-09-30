package ru.otus.dataprocessor;

import com.google.gson.stream.JsonReader;
import ru.otus.model.Measurement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;

public class FileLoader implements Loader {

    private final String filename;

    public FileLoader(String fileName) {
        this.filename = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (JsonReader reader = new JsonReader(new FileReader(filename))) {
            var gson = new Gson();
            Measurement[] objArray = gson.fromJson(reader, Measurement[].class);
            return Arrays.asList(objArray);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
