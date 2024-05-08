package com.example.whatsapp.model.enums;

public enum MessageResponse {
    THUMB_UP("thumbup"),
    LOVE("love"),
    CRYING("crying"),
    SURPRISED("surprised");

    public final String label;

    MessageResponse(String label) {
        this.label = label;
    }

}
