package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Message;
import Service.MessageService;
import Model.Account;
import Service.AccountService;
import java.util.List;

public class SocialMediaController {

    MessageService messageService;
    AccountService accountService;

    public SocialMediaController() {
        messageService = new MessageService();
        accountService = new AccountService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::createMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/register", this::registerAccountHandler);
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
        ObjectMapper mapper = new ObjectMapper();
        String messageIdString = ctx.pathParam("message_id");
        try {
            int messageId = Integer.parseInt(messageIdString);
            Message messageToDelete = messageService.getMessageById(messageId);
            if (messageToDelete != null) {
                boolean deleted = messageService.deleteMessage(messageId);
                if (deleted) {
                    ctx.result(mapper.writeValueAsString(messageToDelete));
                }
            } else {
                ctx.status(200).result("");
            }
        } catch (NumberFormatException ex) {
            ctx.status(400);
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void getAllMessagesForUserHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        String accountIdString = ctx.pathParam("account_id");
        int postedById = Integer.parseInt(accountIdString);
        List<Message> messageList = messageService.getMessageByAccountId(postedById);
        try {
            String jsonResponse = mapper.writeValueAsString(messageList);
            ctx.json(jsonResponse);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messageList = messageService.getAllMessages();
        try {
            String jsonResponse = mapper.writeValueAsString(messageList);
            ctx.json(jsonResponse);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    private void getMessageByIdHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        String messageIdString = ctx.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdString);
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            ctx.status(200);
        } else {
            try {
                String jsonResponse = mapper.writeValueAsString(message);
                ctx.json(jsonResponse);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateMessageByIdHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String messageIdString = ctx.pathParam("message_id");
            int messageId = Integer.parseInt(messageIdString);
            Message getMessage = messageService.getMessageById(messageId);
            if (getMessage == null){
                ctx.status(400);
            } else {
                Message updatedMessage = mapper.readValue(ctx.body(), Message.class);
                if (updatedMessage.message_text.isEmpty() || updatedMessage.message_text.length() > 254) {
                    ctx.status(400);
                } else {
                    getMessage.setMessage_text(updatedMessage.getMessage_text());
                    Message savedMessage = messageService.updateMessageById(getMessage);
                    String jsonResponse = mapper.writeValueAsString(savedMessage);
                    ctx.json(jsonResponse);
                }
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            ctx.status(500);
        }
    }

    private void loginAccountHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account loggedInAccount = accountService.loginAccount(account);
            if (loggedInAccount != null) {
                String jsonResponse = mapper.writeValueAsString(loggedInAccount);
                ctx.json(jsonResponse);
            } else {
                ctx.status(401); 
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    private void registerAccountHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account newAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
            if (newAccount == null) {
                ctx.status(400);
            } else {
                ctx.json(newAccount);
                ctx.status(200);
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }
}


