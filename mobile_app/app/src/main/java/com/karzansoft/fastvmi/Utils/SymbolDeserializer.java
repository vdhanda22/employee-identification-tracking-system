package com.karzansoft.fastvmi.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.karzansoft.fastvmi.extended.CircleMarker;
import com.karzansoft.fastvmi.extended.CrossMarker;
import com.karzansoft.fastvmi.extended.LineMarker;
import com.karzansoft.fastvmi.extended.SymbolMarker;

import java.lang.reflect.Type;

/**
 * Created by Yasir on 4/13/2016.
 */
public class SymbolDeserializer implements JsonDeserializer<SymbolMarker>{
    @Override
    public SymbolMarker deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

       SymbolMarker symbolMarker;

        JsonObject jsonObject=json.getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        int type = jsonObject.get("markerType").getAsInt();
        if (type == Constant.SYMBOL_MARKER_LARGE_DENT || type == Constant.SYMBOL_MARKER_SMALL_DENT) {

            CircleMarker circleMarker = new  CircleMarker(jsonObject.get("startX").getAsInt(),jsonObject.get("startY").getAsInt(), jsonObject.get("markerType").getAsInt(), jsonObject.get("color").getAsInt(),jsonObject.get("scaleFactor").getAsFloat(),jsonObject.get("isLarge").getAsBoolean());
            circleMarker.anchor = jsonObject.get("anchor").getAsFloat();
            symbolMarker = circleMarker;
        }else if(type == Constant.SYMBOL_MARKER_CRACK)
        {
            CrossMarker crossMarker = new CrossMarker(jsonObject.get("startX").getAsInt(),jsonObject.get("startY").getAsInt(), jsonObject.get("markerType").getAsInt(), jsonObject.get("color").getAsInt(),jsonObject.get("scaleFactor").getAsFloat());
            symbolMarker = crossMarker;
        }
        else {

            LineMarker lineMarker = new LineMarker(jsonObject.get("startX").getAsInt(), jsonObject.get("startY").getAsInt(),jsonObject.get("endX").getAsInt(), jsonObject.get("endY").getAsInt(), jsonObject.get("markerType").getAsInt(),jsonObject.get("color").getAsInt(),jsonObject.get("scaleFactor").getAsFloat());
            lineMarker.selectedPoint = jsonObject.get("selectedPoint").getAsInt();
            symbolMarker = lineMarker;
        }
        symbolMarker.radious = jsonObject.get("radious").getAsInt();//isEditable
        symbolMarker.isEditable= jsonObject.get("isEditable").getAsBoolean();
        symbolMarker.isSelected = jsonObject.get("isSelected").getAsBoolean();
        symbolMarker.id = jsonObject.get("id").getAsString();



        return symbolMarker;
    }
}
