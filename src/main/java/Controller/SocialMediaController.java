package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Message;
import Service.MessageService;

public class SocialMediaController {

    MessageService messageService;
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/messages", this::createMessageHandler);

        return app;
    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        if(newMessage == null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(newMessage));
        }
    }
}