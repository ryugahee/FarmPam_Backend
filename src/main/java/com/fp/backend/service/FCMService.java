package com.fp.backend.service;

import com.fp.backend.dto.FCMRequestDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FCMService {

//
//    private final FirebaseMessaging firebaseMessaging;
//    private final UsersRepository usersRepository; // 유저 레포 가져오기
//
//    public String sendByToken(FCMRequestDto requestDto){
//        Optional<Users> user = usersRepository.findById(requestDto.getTargetUserId());
//
//        if(user.isPresent()){
//            //해당 유저의 FirebaseToken유무 확인
//            if(user.get().getFirebaseToken() != null){
//                Notification notification = Notification.builder()
//                        .setTitle(requestDto.getTitle())
//                        .setBody(requestDto.getBody())
//                        .build();
//
//                //메세지 빌드
//                Message message = Message.builder()
//                        .setToken(user.get().getFirebaseToken())
//                        .setNotification(notification)
//                        .build();
//                try{
//                    firebaseMessaging.send(message);
//                    return "알림 전송 성공"+requestDto.getTargetUserId();
//                } catch (FirebaseMessagingException e) {
//                    e.printStackTrace();
//                    return"알림 전송 실패"+requestDto.getTargetUserId();
//                }
//            }else{
//                return "FirebaseToken 없음"+requestDto.getTargetUserId();
//            }
//        }else {
//            return"해당유저 존재하지 않음"+requestDto.getTargetUserId();
//        }
//    }

}
