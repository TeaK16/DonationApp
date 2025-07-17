package finki.ukim.mk.diplomska.web.controller;


import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.ChatMessage;
import finki.ukim.mk.diplomska.model.ChatRoom;
import finki.ukim.mk.diplomska.model.dto.ChatMessageDto;
import finki.ukim.mk.diplomska.model.dto.ChatMessageInputDto;
import finki.ukim.mk.diplomska.model.dto.ChatRoomResponseDto;
import finki.ukim.mk.diplomska.model.exception.EmailNotFoundException;
import finki.ukim.mk.diplomska.model.exception.UserNotFoundException;
import finki.ukim.mk.diplomska.service.ChatService;
import finki.ukim.mk.diplomska.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageInputDto chatMessageInputDto, Principal principal) {
        try {
            ApplicationUser sender = userService.findUserByEmail(principal.getName());
            ApplicationUser recipient = userService.findById(UUID.fromString(chatMessageInputDto.getRecipientId()));

            ChatRoom chatRoom = chatService.getOrCreateChatRoom(sender, recipient);

            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setSender(sender);
            chatMessageDto.setRecipient(recipient);
            chatMessageDto.setContent(chatMessageInputDto.getContent());
            chatMessageDto.setChatRoom(chatRoom);

            chatService.sendChatMessage(chatMessageDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String chatRoomId) {
        return ResponseEntity.ok(chatService.getChatHistory(chatRoomId));
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ChatRoomResponseDto>> getUserConversations(@PathVariable String userId) {
            return ResponseEntity.ok(chatService.getChatList(UUID.fromString(userId)));
    }

    @GetMapping("/chatRoom/{userId}")
    public ChatRoom findChatRoom(@PathVariable String userId, Principal principal){
        ApplicationUser applicationUserSender = null;
        ApplicationUser applicationUserRecipient= null;

        try {
            applicationUserSender = userService.findUserByEmail(principal.getName());
            applicationUserRecipient = userService.findById(UUID.fromString(userId));
        } catch (EmailNotFoundException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return chatService.getOrCreateChatRoom(applicationUserSender, applicationUserRecipient);
    }





}
