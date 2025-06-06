package com.example.demo.dto.Sala;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import com.example.demo.user.dto.UserView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface SalaBasicProjection {
    Integer getId();
    String getName();
    UserView getOwner();

    @JsonIgnore
    LocalDateTime getCreatedAt();

    @JsonProperty("createdAt")
    default String getCreatedAtFormatted() {
        LocalDateTime fecha = getCreatedAt();
        if (fecha == null) return null;

        int day = fecha.getDayOfMonth();
        String month = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int year = fecha.getYear();
        int hour = fecha.getHour();
        int minute = fecha.getMinute();

        // Formatear minutos para que tenga dos dígitos
        String minutos = String.format("%02d", minute);

        return String.format("%d %s %d - Hrs:%d:%s", day, month, year, hour, minutos);
    }
}
