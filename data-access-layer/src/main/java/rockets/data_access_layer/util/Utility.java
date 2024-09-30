package rockets.data_access_layer.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Utility {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
