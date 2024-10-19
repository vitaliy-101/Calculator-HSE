package org.example;

public class MessageCreator {
    public void sendMessage(String message, MessageType type) {
        if (type.equals(MessageType.NEXT)) {
            System.out.println(message);
            return;
        }
        System.out.print(message);
    }
}
