package tkaxv7s.xposed.sesame.util;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooperationIdMap {

    private static final Map<String, String> idMap = new ConcurrentHashMap<>();

    private static final Map<String, String> readOnlyIdMap = Collections.unmodifiableMap(idMap);

    public static Map<String, String> getMap() {
        return readOnlyIdMap;
    }

    public static synchronized void add(String key, String value) {
        idMap.put(key, value);
    }

    public static synchronized void remove(String key) {
        idMap.remove(key);
    }

    public static synchronized void load(String userId) {
        idMap.clear();
        try {
            String body = FileUtil.readFromFile(FileUtil.getCooperationIdMapFile(userId));
            if (!body.isEmpty()) {
                Map<String, String> newMap = JsonUtil.parseObject(body, new TypeReference<Map<String, String>>() {
                });
                idMap.putAll(newMap);
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }

    public static synchronized boolean save(String userId) {
        return FileUtil.write2File(JsonUtil.toNoFormatJsonString(idMap), FileUtil.getCooperationIdMapFile(userId));
    }

}
