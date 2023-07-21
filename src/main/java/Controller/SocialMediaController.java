package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Message;
import Service.MessageService;

public class SocialMediaController {

    MessageService messageService;

    public SocialMediaController() {
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/messages", this::createMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        return app;
    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        if(newMessage == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(newMessage));
        }
    }

    private void deleteMessageHandler(Context ctx) {
        String messageIdString = ctx.pathParam("message_id");
        int message_id = Integer.parseInt(messageIdString);
        boolean deleted = messageService.deleteMessage(message_id);
        if (deleted) {
            ctx.status(200);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            boolean alsoDeleted = messageService.deleteMessage(message.getMessage_id());
            if (alsoDeleted) {
                ctx.status(200);
            }
        } catch (JsonProcessingException ex) {
            ctx.status(404);
        }
        
    }
}