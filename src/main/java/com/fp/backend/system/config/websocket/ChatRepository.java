package com.fp.backend.system.config.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
@Slf4j
public class ChatRepository {
    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    public void init(){
        chatRoomMap = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom(){
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }
    public ChatRoom findByRoomId(String roomId){
        return chatRoomMap.get(roomId);
    }

    public ChatRoom createChatRoom(String roomName){
        ChatRoom chatRoom = new ChatRoom().create(roomName);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);

        return chatRoom;
    }

    public void increaseUser(String roomId){
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount()+1);
    }
    public void decreaseUser(String roomId){
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount()-1);
    }

    public  String addUser(String roomId, String userName){

        ChatRoom chatRoom = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();
        chatRoom.getUserList().put(userUUID,userName);

        return userUUID;
    }


    public String isDuplicateName(String roomId,String username){

        ChatRoom chatRoom = chatRoomMap.get(roomId);
        String temp = username;


       while(chatRoom.getUserList().containsValue(temp)){
            int ranNum = (int) (Math.random() * 100) + 1;
            temp = username+ranNum;
        }

        return temp;
    }


    public void deleteUser(String roomId,String userUUID){
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        chatRoom.getUserList().remove(userUUID);
    }

    public String getUserName(String roomId,String userUUID){
        ChatRoom chatRoom = chatRoomMap.get(roomId);

        return chatRoom.getUserList().get(userUUID);
    }

    public List<String> getUserList(String roomId){
        List<String> list = new ArrayList<>();

        ChatRoom chatRoom = chatRoomMap.get(roomId);

        chatRoom.getUserList().forEach((key,value) -> list.add(value));

        return list;
    }
}
