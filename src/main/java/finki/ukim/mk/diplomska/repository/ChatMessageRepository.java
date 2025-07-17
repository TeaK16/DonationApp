package finki.ukim.mk.diplomska.repository;

import finki.ukim.mk.diplomska.model.ChatMessage;
import finki.ukim.mk.diplomska.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findByChatRoomOrderByLocalDateTimeAsc(ChatRoom chatRoom);
}
