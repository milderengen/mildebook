package com.example.wannabemessenger;

import com.example.wannabemessenger.objectables.*;
import com.example.wannabemessenger.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class generalFunctions {

    public List<user> findFriends(user user, friendshipsService friendshipsService){
        List<friendship> friendships = friendshipsService.getAllFriendships();
        System.out.println(friendships);
        if(friendships.isEmpty()){
            return Collections.emptyList();
        }
        return friendships.stream()
                .filter(friendship -> friendship.getFriend().equals(user) || friendship.getPerson().equals(user))
                .map(friendship -> {
                    if (friendship.getPerson().equals(user)) {
                        return friendship.getFriend();
                    } else {
                        return friendship.getPerson();
                    }
                })
                .collect(Collectors.toList());
    }
    public user myUserFromSession(HttpSession session, usersService usersService){
        int userID = (Integer) session.getAttribute("userID");
        Optional<user> user = usersService.findUserByID(userID);
        return user.get();
    }
    public Timestamp getTime(){
        long currentTimeMillis = System.currentTimeMillis();
        return new Timestamp(currentTimeMillis);
    }
    public reaction fillReaction(String emotion, post post, HttpSession session, usersService usersService){
        reaction reaction = new reaction();
        reaction.setPost(post);
        reaction.setUser(myUserFromSession(session, usersService));
        reaction.setTimestamp(getTime());
        if(emotion.equals("positive")){
            reaction.setReactionType("like");
        }else{reaction.setReactionType("dislike");}
        return reaction;
    }
    public Optional<chat> isThereAChat(user friend, List<chat> chats){
        return chats.stream()
                .filter(chat -> chat.getChatter1().equals(friend) || chat.getChatter2().equals(friend))
                .findAny();
    }
    public int checkFile(MultipartFile file){
        final long MAX_SIZE = 1024 * 1024 * 5; // 5 MB
        if (file.isEmpty()) {
            return 1;
        }

        // Check the file type
        String contentType = file.getContentType();
        if (!isAllowedContentType(contentType)) {
            return 2;
        }

        // Check the file size
        long size = file.getSize();
        if (size > MAX_SIZE) {
            return 3;
        }
        return 0;
    }
    private boolean isAllowedContentType(String contentType) {
        // Define allowed content types and check
        return "image/jpeg".equals(contentType) || "image/png".equals(contentType);
    }

    public String generateSafeFileName(String originalFileName) {
        // Generate a safe file name, could use UUIDs
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
    public int randNum(){
        Random rand = new Random();
        return rand.nextInt(100);
    }
    public friendship dummyFriendship(){
        user friend1 = new user();
        user user = new user();
        user.setId(5);
        friend1.setId(10);
        friendship friendship1 = new friendship();friendship1.setPerson(user);friendship1.setFriend(friend1);friendship1.setId(1);
        return friendship1;
    }
    public comment dummyComment(){
        Random rand = new Random();
        int randomNum = rand.nextInt(100);
        comment comment = new comment();
        comment.setId(randomNum);
        comment.setPosted(getTime());
        comment.setOriginalCommentId(comment.getId());
        comment.setAuthor(dummmyUser());
        return comment;
    }
    public List<comment> comments(){
        List<comment> comments = new ArrayList<>();
        for(int i = 0;i<randNum();i++){
            comments.add(dummyComment());
        }
        return comments;
    }
    public post activeDummyPost(){
        post post = new post();
        post.setId(randNum());
        post.setHeadline("headline");
        post.setContent("content");
        post.setOriginalPostId(post.getId());
        post.setDislikes(5);
        post.setLikes(6);
        post.setAuthor(dummmyUser());
        post.setTimestamp(getTime());
        post.setActive(true);
        post.setComments(comments());
        return post;
    }
//    public post modifiedActiveDummyPost(){
//        post post = new post();
//        post.setId(9);
//        post.setHeadline("headline");
//        post.setContent("content");
//        post.setOriginalPostId(post.getId());
//        post.setDislikes(5);
//        post.setLikes(6);
//        post.setAuthor(dummmyUser());
//        post.setTimestamp(getTime());
//        post.setActive(false);
//        post.setDateOfModification(getTime());
//
//    }
    public message dummyMessage(){
        message message = new message();
        message.setId(randNum());
        message.setAuthor(dummmyUser());
        message.setChat(dummyChat());
        message.setTimestamp(getTime());

        return message;
    }
    public chat dummyChat(){
        chat chat = new chat();
        chat.setId(randNum());
        chat.setChatter2(dummmyUser());
        chat.setChatter1(dummmyUser());
        List<message> messages = new ArrayList<>();
        messages.add(dummyMessage());
        chat.setMessages(messages);
        return chat;
    }
    public user dummmyUser(){

        user user = new user();
            user.setImg("image.jpg");
            user.setBio("my bio is the best bio");
            user.setPrivateAcc(false);
            user.setNumOfFriends(randNum());
            user.setId(randNum());
            user.setName("dummy");
            user.setPassword("password");
        friendship friendship1 = dummyFriendship();
        friendship friendship2 = dummyFriendship();
        List<friendship> friends = new ArrayList<>();
        friends.add(friendship1);friends.add(friendship2);
        user.setFriendships(friends);
        post post = activeDummyPost();
        List<post> posts = new ArrayList<>();
        posts.add(post);
        user.setPosts(posts);
        List<message> messages = new ArrayList<>();
        message message = dummyMessage();
        messages.add(message);
        user.setMessages(messages);
        return user;
    }
}
