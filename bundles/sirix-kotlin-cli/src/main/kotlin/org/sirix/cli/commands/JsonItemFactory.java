package org.sirix.cli.commands;

import org.brackit.xquery.atomic.*;
import org.brackit.xquery.xdm.Item;
import org.sirix.api.json.JsonNodeReadOnlyTrx;
import org.sirix.xquery.json.*;

import java.math.BigDecimal;

public final class JsonItemFactory {
    public JsonItemFactory() {
    }

    public Item getSequence(final JsonNodeReadOnlyTrx rtx, final JsonDBCollection collection) {
        switch (rtx.getKind()) {
            case ARRAY:
                return new JsonDBArray(rtx, collection);
            case OBJECT:
                return new JsonDBObject(rtx, collection);
            case OBJECT_KEY:
                return new AtomicJsonDBItem(rtx, collection, rtx.getName());
            case STRING_VALUE:
            case OBJECT_STRING_VALUE:
                return new AtomicJsonDBItem(rtx, collection, new Str(rtx.getValue()));
            case BOOLEAN_VALUE:
            case OBJECT_BOOLEAN_VALUE:
                return new AtomicJsonDBItem(rtx, collection, new Bool(rtx.getBooleanValue()));
            case OBJECT_NULL_VALUE:
            case NULL_VALUE:
                return new AtomicJsonDBItem(rtx, collection, new Null());
            case OBJECT_NUMBER_VALUE:
            case NUMBER_VALUE:
                final Number number = rtx.getNumberValue();

                if (number instanceof Integer) {
                    return new NumericJsonDBItem(rtx, collection, new Int32(number.intValue()));
                } else if (number instanceof Long) {
                    return new NumericJsonDBItem(rtx, collection, new Int64(number.intValue()));
                } else if (number instanceof Float) {
                    return new NumericJsonDBItem(rtx, collection, new Flt(number.floatValue()));
                } else if (number instanceof Double) {
                    return new NumericJsonDBItem(rtx, collection, new Dbl(number.doubleValue()));
                } else if (number instanceof BigDecimal) {
                    return new NumericJsonDBItem(rtx, collection, new Dec((BigDecimal) number));
                }
                // $CASES-OMITTED$
            default:
                new AssertionError();
        }

        return null;
    }
}