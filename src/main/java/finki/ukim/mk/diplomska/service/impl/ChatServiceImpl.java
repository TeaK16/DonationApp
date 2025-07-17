package finki.ukim.mk.diplomska.service.impl;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.ChatMessage;
import finki.ukim.mk.diplomska.model.ChatRoom;
import finki.ukim.mk.diplomska.model.dto.ChatMessageDto;
import finki.ukim.mk.diplomska.model.dto.ChatRoomResponseDto;
import finki.ukim.mk.diplomska.model.exception.UserNotFoundException;
import finki.ukim.mk.diplomska.repository.ChatMessageRepository;
import finki.ukim.mk.diplomska.repository.ChatRoomRepository;
import finki.ukim.mk.diplomska.service.ChatService;
import finki.ukim.mk.diplomska.service.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;


    public ChatServiceImpl(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, SimpMessagingTemplate simpMessagingTemplate, UserService userService) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }



    @Transactional
    @Override
    public ChatRoom getOrCreateChatRoom(ApplicationUser sender, ApplicationUser recipient) {
        Optional<ChatRoom> chatRoom1 = chatRoomRepository.findById(String.format("%s_%s", sender.getUuid().toString(), recipient.getUuid().toString()));
        Optional<ChatRoom> chatRoom2 = chatRoomRepository.findById(String.format("%s_%s", recipient.getUuid().toString(),sender.getUuid().toString()));

        if(chatRoom1.isPresent()) return chatRoom1.get();
        if(chatRoom2.isPresent()) return chatRoom2.get();


        String chatId = String.format("%s_%s", sender.getUuid().toString(), recipient.getUuid().toString());

        ChatRoom chatRoom  = new ChatRoom(chatId);


        return chatRoomRepository.save(chatRoom);

    }

    @Override
    public ChatMessage sendChatMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatMessageDto.getChatRoom());
        chatMessage.setSender(chatMessageDto.getSender());
        chatMessage.setRecipient(chatMessageDto.getRecipient());
        chatMessage.setContent(chatMessageDto.getContent());
        chatMessage.setLocalDateTime(LocalDateTime.now());

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", savedMessage.getId());
        payload.put("content", savedMessage.getContent());
        payload.put("senderId", savedMessage.getSender().getUuid().toString());
        payload.put("senderEmail", savedMessage.getSender().getEmail());
        payload.put("recipientId", savedMessage.getRecipient().getUuid().toString());
        payload.put("timestamp", savedMessage.getLocalDateTime().toString());
        payload.put("chatRoomId", savedMessage.getChatRoom().getChatId());


       simpMessagingTemplate.convertAndSend("/queue/messages/" +
                       chatMessageDto.getChatRoom().getChatId(), payload);



        return chatMessage;
    }

    @Override
    public List<ChatMessage> getChatHistory(String chatRoomId){
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByChatId(chatRoomId);
        if(chatRoom.isPresent()) {
            return chatMessageRepository.findByChatRoomOrderByLocalDateTimeAsc(chatRoom.get());
        } else {
            return null;
        }
    }

    @Override
    public List<ChatRoomResponseDto> getChatList(UUID userId) {
        List<ChatRoom> chatRooms1 = this.chatRoomRepository.findByChatIdLike(userId + "_%");
        List<ChatRoom> chatRooms2 = this.chatRoomRepository.findByChatIdLike("%_" + userId);

        List<ChatRoom> chatRooms = Stream.concat(chatRooms1.stream(), chatRooms2.stream()).toList();
        List<ChatRoomResponseDto> rooms = new ArrayList<>();

       for(ChatRoom cr: chatRooms){
           String[] parts = cr.getChatId().split("_");
           String otherUserId = parts[0].equals(userId.toString()) ? parts[1] : parts[0];
           ApplicationUser user = null;
           try {
               user = this.userService.findById(UUID.fromString(otherUserId));
           } catch (UserNotFoundException e) {
               throw new RuntimeException(e);
           }
           ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(user.getUuid(),user.getUsername(), cr.getChatId());

           rooms.add(chatRoomResponseDto);
       }

       return rooms;
    }

    @Override
    public String findChatRoomById(String chatRoomId) {

        Optional<ChatRoom> chatRoom = chatRoomRepository.findByChatId(chatRoomId);

        if(chatRoom.isPresent()){
            return chatRoom.get().getChatId();
        }else {
            return null;
        }

    }

}
