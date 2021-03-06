package com.fasterxml.jackson.module.jsonSchema;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

/**
 * Trivial test to ensure {@link JsonSchema} can be also deserialized
 */
public class TestReadJsonSchema
    extends SchemaTestBase
{
    enum SchemaEnum { YES, NO; }

    static class Schemable {
        public String name;
        public char[] nameBuffer;

        // We'll include tons of stuff, just to force generation of schema
        public boolean[] states;
        public byte[] binaryData;
        public short[] shorts;
        public int[] ints;
        public long[] longs;

        public float[] floats;
        public double[] doubles;

        public Object[] objects;
        public JsonSerializable someSerializable;

        public Iterable<Object> iterableOhYeahBaby;

        public List<String> extra;
        public ArrayList<String> extra2;
        public Iterator<String[]> extra3;

        public Map<String,Double> sizes;
        public EnumMap<SchemaEnum,List<String>> whatever;

        SchemaEnum testEnum;
        public EnumSet<SchemaEnum> testEnums;
    }

    /**
     * Verifies that a simple schema that is serialized can be
     * deserialized back to equal schema instance
     */
    public void testDeserializeSimple() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        mapper.acceptJsonFormatVisitor(mapper.constructType(Schemable.class), visitor);
        JsonSchema jsonSchema = visitor.finalSchema();
        assertNotNull(jsonSchema);

        String schemaStr = mapper.writeValueAsString(jsonSchema);
        assertNotNull(schemaStr);
        JsonSchema result = mapper.readValue(schemaStr, JsonSchema.class);
        assertEquals("Trying to read from '"+schemaStr+"'", jsonSchema, result);
    }
}
