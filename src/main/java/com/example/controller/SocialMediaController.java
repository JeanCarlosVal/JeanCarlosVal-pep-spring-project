package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.containers.AccountContainer;
import com.example.containers.MessageContainer;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AccountNotFoundException;
import com.example.exception.CreateMessageFailedException;
import com.example.exception.RegisterAccountFailedException;
import com.example.exception.UpdateMessageException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@Controller
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Account Mappings
     */

    @PostMapping("register")
    public @ResponseBody ResponseEntity<Account> registerAccount(@RequestBody Account account)
            throws RegisterAccountFailedException {

        AccountContainer<Account> newAccount = accountService.registerAccount(account);

        if (newAccount.getErrorMessage() != null) {
            throw new RegisterAccountFailedException(newAccount.getErrorMessage(), newAccount.getErrorCode());
        }

        return ResponseEntity.status(200).body(newAccount.getValue());

    }

    @PostMapping("login")
    public @ResponseBody ResponseEntity<Account> logInAccount(@RequestBody Account account)
            throws AccountNotFoundException {
        AccountContainer<Account> loggedAccount = accountService.logIn(account);

        if (loggedAccount.getErrorMessage() != null) {
            throw new AccountNotFoundException(loggedAccount.getErrorMessage(), loggedAccount.getErrorCode());
        }

        return ResponseEntity.status(200).body(loggedAccount.getValue());
    }

    /**
     * Message Mappings
     */

    @PostMapping("messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message message)
            throws CreateMessageFailedException {
        MessageContainer<Message> newMessage = messageService.createMessage(message);

        if (newMessage.getErrorMessage() != null) {
            throw new CreateMessageFailedException(newMessage.getErrorMessage(), newMessage.getErrorCode());
        }

        return ResponseEntity.status(200).body(newMessage.getValue());
    }

    @GetMapping("messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    @GetMapping("messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessagesById(@PathVariable int message_id) {
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.status(200).body((message.getMessageId() != null) ? message : null);
    }

    @DeleteMapping("messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable int message_id) {
        int numOfRows = messageService.deleteMessageById(message_id);
        return ResponseEntity.status(200).body((numOfRows != 0) ? numOfRows : null);
    }

    @PatchMapping("messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> updateMessage(@PathVariable int message_id,
            @RequestBody Message message_text) throws UpdateMessageException {
        MessageContainer<Message> message = messageService.updateMessage(message_id, message_text);

        if (message.getErrorMessage() != null) {
            throw new UpdateMessageException(message.getErrorMessage(), message.getErrorCode());
        }

        return ResponseEntity.status(200).body(1);
    }

    @GetMapping("accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessagesFromUser(@PathVariable int account_id) {
        return ResponseEntity.status(200).body(messageService.getAllMessagesFromUser(account_id));
    }

    /**
     * Exception Handlers
     */
    @ExceptionHandler(RegisterAccountFailedException.class)
    public @ResponseBody ResponseEntity<String> registerAccountFailedException(RegisterAccountFailedException ex) {
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public @ResponseBody ResponseEntity<String> accountNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }

    @ExceptionHandler(CreateMessageFailedException.class)
    public @ResponseBody ResponseEntity<String> createMessageFailedException(CreateMessageFailedException ex) {
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }

    @ExceptionHandler(UpdateMessageException.class)
    public @ResponseBody ResponseEntity<String> updateMessageException(UpdateMessageException ex) {
        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());
    }
}
