package com.group_1.usege.utilities.localStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.group_1.usege.utilities.interfaces.TriConsumer;
import com.group_1.usege.utilities.parser.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * <pre>
 * Class for putting and getting value from shared reference storage
 * Use {@link Inject} to inject the singleton instance
 * </pre>
 */
@Singleton
public class SharedRefLocalStorage {
    private final String SHARED_REF = "USEGE";
    public final SharedPreferences sharedPreferences;
    private final Map<Class, TriConsumer<SharedPreferences.Editor, String, Object>> putCallbacks;
    private JsonParser jsonParser;
    @Inject
    public SharedRefLocalStorage(@ApplicationContext Context appContext, JsonParser jsonParser)
    {
        this.jsonParser = jsonParser;
        sharedPreferences =  appContext.getSharedPreferences(SHARED_REF, Context.MODE_PRIVATE);
        
        putCallbacks = new HashMap<>();
        putCallbacks.put(Integer.class, (editor, s, o) -> editor.putInt(s, (Integer) o));
        putCallbacks.put(int.class, (editor, s, o) -> editor.putInt(s, (int)o));
        putCallbacks.put(String.class, (editor, s, o) -> editor.putString(s, (String)o));
        putCallbacks.put(Long.class, (editor, s, o) -> editor.putLong(s, (Long) o));
        putCallbacks.put(long.class, (editor, s, o) -> editor.putLong(s, (long)o));
        putCallbacks.put(Float.class, (editor, s, o) -> editor.putFloat(s, (Float)o));
        putCallbacks.put(float.class, (editor, s, o) -> editor.putFloat(s, (float)o));
        putCallbacks.put(Boolean.class, (editor, s, o) -> editor.putBoolean(s, (Boolean)o));
        putCallbacks.put(boolean.class, (editor, s, o) -> editor.putBoolean(s, (boolean)o));
    }

    /**
     * For get json object. For get primitives value, please access to {@link #sharedPreferences}
     * @param key the key
     * @param objClass class of the object
     * @return the associated object (null if nothing found)
     * @param <S> class of the object
     */
    public <S> S getObject(String key, Class<S> objClass)
    {
        String json = sharedPreferences.getString(key, null);
        if (json == null)
            return null;
        return jsonParser.fromJson(json, objClass);
    }

    public void delete(String key)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
    /**
     * Put any type of value in the associated key
     * @param key key
     * @param value value
     */
    public void putValue(String key, Object value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TriConsumer<SharedPreferences.Editor, String, Object> predefinedPutCallback = putCallbacks.getOrDefault(value.getClass(), null);
        if (predefinedPutCallback != null)
            predefinedPutCallback.accept(editor, key, value);
        else
        {
            //Try to put json
            try
            {
                String json = jsonParser.toJson(value);
                editor.putString(key, json);
            }
            catch (Exception e)
            {
                Log.e("Put local storage", e.getMessage());
                return;
            }
        }
        editor.apply();
    }
}
