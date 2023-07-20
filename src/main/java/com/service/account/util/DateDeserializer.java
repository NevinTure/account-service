package com.service.account.util;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateS = p.getValueAsString();
        dateS = "01-" + dateS;
        return LocalDate.parse(dateS, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
