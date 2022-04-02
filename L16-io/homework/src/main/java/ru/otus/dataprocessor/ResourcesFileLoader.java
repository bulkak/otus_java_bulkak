package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String filename;

    public static class MeasurementDeserializer extends StdDeserializer<Measurement> {

        public MeasurementDeserializer() {
            this(null);
        }
        public MeasurementDeserializer(Class<?> vc) {
            super(vc);
        }
        @Override
        public Measurement deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
            ObjectCodec codec = parser.getCodec();
            JsonNode node = codec.readTree(parser);

            // try catch block
            String name = node.get("name").asText();
            double value = node.get("value").asDouble();
            return new Measurement(name, value);
        }
    }
    public ResourcesFileLoader(String fileName) {
        this.filename = fileName;
        SimpleModule module = new SimpleModule(
            "MeasurementDeserializer",
            new Version(1, 0, 0, null, null, null)
        );
        module.addDeserializer(Measurement.class, new MeasurementDeserializer());
        mapper.registerModule(module);
    }

    @Override
    public List<Measurement> load() {
        try  {
            Measurement[] objArray = mapper.readValue(
                    ResourcesFileLoader.class.getClassLoader().getResourceAsStream(this.filename),
                    Measurement[].class
            );
            return Arrays.asList(objArray);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
