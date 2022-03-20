package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String filename;
    public ResourcesFileLoader(String fileName) {
        this.filename = fileName;
    }

    @Override
    public List<Measurement> load() {
        try  {
            Measurement[] objArray = mapper.readValue(ResourcesFileLoader.class.getClassLoader().getResourceAsStream(this.filename), Measurement[].class);
            return Arrays.asList(objArray);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
