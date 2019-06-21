package com.duy.adruino.car.connection;

import android.support.annotation.NonNull;

public class Message {
    private final Type type;
    private final String content;

    public Message(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @NonNull
    @Override
    public String toString() {
        return "MessageItem{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

    public enum Type {
        IN, OUT, ERROR
    }

}
