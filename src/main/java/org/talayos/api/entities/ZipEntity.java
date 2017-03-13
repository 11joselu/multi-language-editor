package org.talayos.api.entities;

import lombok.Data;

import java.util.List;


@Data
public class ZipEntity {
    private String message;
    private List<?> data;
    private int statusCode;
}
