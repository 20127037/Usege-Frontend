package com.group_1.usege.utilities.parser;

import android.util.Log;

import com.google.gson.Gson;

import java.io.Reader;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <pre>
 * Class for working with JSON
 * Use {@link Inject} to inject the singleton instance
 * </pre>
 */
@Singleton
public class JsonParser {

    private final Gson wrappedGson;

    public JsonParser()
    {
        wrappedGson = new Gson();
    }

    public <S> S fromJson(String json, Class<S> type)
    {
        try
        {
            return wrappedGson.fromJson(json, type);
        }
        catch (Exception e)
        {
            Log.e("Json parsed fail", e.getMessage());
            return null;
        }
    }

    public <S> S fromJson(Reader json, Class<S> type)
    {
        try
        {
            return wrappedGson.fromJson(json, type);
        }
        catch (Exception e)
        {
            Log.e("Json parsed fail", e.getMessage());
            return null;
        }
    }


    public String toJson(Object value)
    {
        if (value == null)
            return null;
        if (value instanceof String)
            return (String)value;
        return wrappedGson.toJson(value);
    }
}
