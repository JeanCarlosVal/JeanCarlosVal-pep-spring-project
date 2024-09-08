package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.containers.MessageContainer;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public MessageContainer<Message> createMessage(Message message) {
        MessageContainer<Message> newMessage = new MessageContainer<>();

        String text = message.getMessageText();
        boolean userExists = accountRepository.existsById(message.getPostedBy());

        if (text == "") {
            newMessage.setErrorCode(400);
            newMessage.setErrorMessage("The text is empty");
            return newMessage;
        }

        if (text.length() >= 255) {
            newMessage.setErrorCode(400);
            newMessage.setErrorMessage("The text size is too big");
            return newMessage;
        }

        if (userExists == false) {
            newMessage.setErrorCode(400);
            newMessage.setErrorMessage("The posted by user does not exist");
            return newMessage;
        }

        newMessage.setValue(messageRepository.save(message));

        return newMessage;
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(new Message());
    }

    public int deleteMessageById(int id) {

        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        } else {
            return 0;
        }
    }

    public MessageContainer<Message> updateMessage(int id, Message req) {
        MessageContainer<Message> updatedMessage = new MessageContainer<>();

        String text = req.getMessageText();

        if (text.length() >= 255) {
            updatedMessage.setErrorMessage("text is too long");
            updatedMessage.setErrorCode(400);
            return updatedMessage;
        }

        if (text == "") {
            updatedMessage.setErrorCode(400);
            updatedMessage.setErrorMessage("text should not be empty");
            return updatedMessage;
        }

        if (messageRepository.existsById(id)) {
            Message message = messageRepository.findById(id).get();
            message.setMessageText(text);
            updatedMessage.setValue(messageRepository.save(message));
            return updatedMessage;
        } else {
            updatedMessage.setErrorMessage("The given id does not exists");
            updatedMessage.setErrorCode(400);
            return updatedMessage;
        }

    }

}
