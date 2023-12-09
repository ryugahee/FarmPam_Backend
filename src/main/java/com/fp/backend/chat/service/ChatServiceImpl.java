package com.fp.backend.chat.service;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.auction.entity.Item;
import com.fp.backend.auction.entity.ItemImg;
import com.fp.backend.auction.repository.ItemImgRepository;
import com.fp.backend.auction.repository.ItemRepository;
import com.fp.backend.chat.domain.Chat;
import com.fp.backend.chat.domain.Message;
import com.fp.backend.chat.dto.ChatDetailInfoDTO;
import com.fp.backend.chat.dto.ChatPreviewInfoDTO;
import com.fp.backend.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final UserRepository userRepository;

    @Override
    public List<Long> getIds(String userId) {
        log.info("{}의 채팅 내역 검색", userId);
        return chatRepository.findChatsByFirstUserIdOrSecondUserId(userId)
                .stream().map(Chat::getChatId).collect(Collectors.toList());
    }

    @Override
    public List<ChatPreviewInfoDTO> getChatPreviewInfos(List<Long> chatIds, String userId) {
        List<ChatPreviewInfoDTO> result = new ArrayList<>();

        // TODO: chatIds를 Chat 도큐먼트에 firstUserId, secondUserId, itemId, lastMessage 찾은 후
        // TODO: itemId로 경매 물품 타이틀, 경매 이미지 가져오기
        // TODO: firstUserId와 secondUserId를 자신의 아이디랑 비교 후 상대방 프로필 사진과 닉네임 가져오기
        // TODO: ChatPriviewDTO 생성

        for (Long chatId : chatIds) {
            Chat chat = getChat(chatId);

            // 상대방 찾기
            String receiverId = getReceiverId(chat, userId);

            // TODO: User Entity 작업 완료 시 아래 주석 풀 것
//            Users receiver = userRepository.findById(receiverId)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자를 찾으려고 합니다."));

            // 경매 정보 찾기
            Item item = itemRepository.findById(chat.getItemId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않은 경매 물품을 찾으려고 합니다."));


            // 경매 썸네일 이미지 찾기
            ItemImg itemImg = itemImgRepository.findByItem(item)
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("경매 이미지가 존재하지 않습니다."));


            // 마지막 메시지 가져오기
            List<Message> messages = chat.getMessages();
            Message message = messages.get(messages.size() - 1);


            // TODO: User Entity 작업 완료 시 아래 주석 풀 것
            result.add(ChatPreviewInfoDTO.builder()
//                    .toNickName(receiver.getNickname())
//                    .toNickNameThumbnailUrl(receiver.getImageUrl())
                    .toNickName("테스트 아이디") // FIXME: 테스트용 지울 것
                    .toNickNameThumbnailUrl("../../public/assets/img/profile1.png")
                    .lastMessage(message.getMessage())
                    .updateTime(message.getUpdateAt())
                    .itemThumbnailUrl(itemImg.getImgUrl())
                    .build());
        }

        return result;
    }

    @Override
    public ChatDetailInfoDTO getChatDetailInfo(Long chatId, String userId) {

        log.info("채팅방 정보 가져오기: getChatDetailInfo");

        Chat chat = getChat(chatId);

        // TODO: 상대방 아이디를 찾은 후 상대방 닉네임 가져오기
        // TODO: itemId 로 타이틀, 썸네일, 경매 현재가(실시간 성 필요)

        String receiverId = getReceiverId(chat, userId);

        // TODO: User Entity 작업 완료 시 아래 주석 풀 것
//            Users receiver = userRepository.findById(receiverId)
//                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자를 찾으려고 합니다."));

        // 경매 정보 찾기
        Item item = itemRepository.findById(chat.getItemId())
                .orElseThrow(() -> new RuntimeException("존재하지 않은 경매 물품을 찾으려고 합니다."));

        ItemImg itemImg = itemImgRepository.findByItem(item)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("경매 이미지가 존재하지 않습니다."));

        // TODO: 경매 현재 입찰가 구현

        return ChatDetailInfoDTO.builder()
        //                    .toNickName(receiver.getNickname())
                .toNickName("테스트 아이디") // FIXME: 구현이 완료되면 지울 것
                .itemTitle(item.getItemTitle())
                .itemThumbnailUrl(itemImg.getImgUrl())
                .biddingPrice(10000L)
                .build();
    }

    @Override
    public List<Message> getChatMessages(Long chatId) {

        Chat chat = getChat(chatId);
        return chat.getMessages();
    }

    private Chat getChat(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 채팅을 찾을려고 합니다."));
    }

    private String getReceiverId(Chat chat, String userId) {
        return Objects.equals(chat.getFirstUserId(), userId) ? chat.getSecondUserId() : chat.getFirstUserId();
    }
}
