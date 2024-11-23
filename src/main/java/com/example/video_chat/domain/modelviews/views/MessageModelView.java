package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageModelView extends MessageModelViewSimple{
    private ConversationModelView toConversation;

    public MessageModelView(Message message) {
        super(message);
        this.toConversation = new ConversationModelView(message.getConversation());
    }


    public ConversationModelView getToConversation() {
        return toConversation;
    }


}
