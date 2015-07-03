package Models;

import org.json.JSONObject;

/**
 * Created by Egor on 03.07.2015.
 */

public interface IJsonInterface<T> {
    public T getFromJson(JSONObject json);
}
