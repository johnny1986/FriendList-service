package com.afilias.friendlist.service;

import com.afilias.friendlist.exception.ResourceNotFoundException;
import com.afilias.friendlist.model.dto.UserDTO;
import com.afilias.friendlist.model.dto.UserWithFriendsDTO;
import com.afilias.friendlist.model.entity.User;
import com.afilias.friendlist.model.view.UserWithFriendsView;
import com.afilias.friendlist.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendListService {

    private UserRepository userRepository;

    private ModelMapper mapper;

    @Autowired
    public FriendListService(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserDTO> getUserList() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> mapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    public List<UserDTO> getUserFriendList(long userId) throws ResourceNotFoundException {
        List<UserWithFriendsView> userWithFriendsList = userRepository.getAllFriendsByUserId(userId);

        if (userWithFriendsList.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        } else {
            return userWithFriendsList.stream().map(element -> {
                return UserDTO.builder().id(element.getFriendId()).name(element.getFriendName()).city(element.getFriendCity()).build();
            }).collect(Collectors.toList());
        }
    }

    public List<UserWithFriendsDTO> getUserSuggestedFriendList(long userId) throws ResourceNotFoundException {
        List<UserWithFriendsView> userWithFriendsList = userRepository.getAllFriendsAndUserByUserId(userId);

        if (userWithFriendsList.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        } else {
            List<Long> friendIdList = userWithFriendsList.stream().map(element -> element.getFriendId()).collect(Collectors.toList());
            List<UserWithFriendsView> friendsList = userRepository.getAllFriendsByUserListId(friendIdList);

            Map<Long, List<UserWithFriendsView>> friendMap = friendsList.stream().collect(Collectors.groupingBy(UserWithFriendsView::getId));

            List<UserWithFriendsDTO> returnList = new ArrayList<>();

            for (Map.Entry<Long, List<UserWithFriendsView>> entry : friendMap.entrySet()) {
                List<UserDTO> listOfFriends = entry.getValue().stream().map(element -> {
                    return UserDTO.builder().id(element.getFriendId()).name(element.getFriendName()).city(element.getFriendCity()).build();
                }).filter(element -> element.getId() != userId).collect(Collectors.toList());

                UserDTO mainUserDTO = mapper.map(entry.getValue().stream().findFirst().get(), UserDTO.class);
                returnList.add(UserWithFriendsDTO.builder().user(mainUserDTO).userFriendsList(listOfFriends).build());
            }

            return returnList;
        }
    }

    public List<UserDTO> getUserSuggestedFriendGeoList(long userId) throws ResourceNotFoundException {
        List<UserWithFriendsView> userWithFriendsList = userRepository.getAllFriendsAndUserByUserId(userId);

        if (userWithFriendsList.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        } else {
            List<Long> friendIdList = userWithFriendsList.stream().map(element -> element.getFriendId()).collect(Collectors.toList());
            List<UserWithFriendsView> friendsList = userRepository.getAllFriendsByUserListId(friendIdList);

            List<UserDTO> listOfFriends = friendsList.stream().map(element -> {
                return UserDTO.builder().id(element.getFriendId()).name(element.getFriendName()).city(element.getFriendCity()).build();
            }).filter(element -> element.getId() != userId).sorted((u1, u2) -> u1.getCity().compareTo(u2.getCity())).collect(Collectors.toList());

            return listOfFriends.stream().distinct().collect(Collectors.toList());
        }
    }


}
