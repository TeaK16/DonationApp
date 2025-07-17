package finki.ukim.mk.diplomska.service;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.ChatMessage;
import finki.ukim.mk.diplomska.model.ChatRoom;
import finki.ukim.mk.diplomska.model.dto.ChatMessageDto;
import finki.ukim.mk.diplomska.model.dto.ChatRoomResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    public ChatRoom getOrCreateChatRoom(ApplicationUser user1, ApplicationUser user2);
    public ChatMessage sendChatMessage(ChatMessageDto chatMessageDto);
    public List<ChatMessage> getChatHistory(String chatRoomId);

    public List<ChatRoomResponseDto> getChatList(UUID userId);
    public String findChatRoomById(String chatRoomId);

}
