package skyfall.server.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameResultStorage {
    private static final String FILE_NAME = "game_results.json";

    private final ObjectMapper mapper;

    public GameResultStorage() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveGameResult(GameResult newResult) {
        List<GameResult> results = loadAllResults();
        results.add(newResult);

        try {
            mapper.writeValue(new File(FILE_NAME), results);
        } catch (IOException e) {
            System.err.println("Failed to save game result: " + e.getMessage());
        }
    }

    private List<GameResult> loadAllResults() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, GameResult.class));
        } catch (IOException e) {
            System.err.println("Failed to load existing results, creating new file");
            return new ArrayList<>();
        }
    }
}
