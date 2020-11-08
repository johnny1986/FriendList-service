package com.afilias.friendlist.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class UserWithFriendsDTO {

    private UserDTO user;

    private List<UserDTO> userFriendsList;
}
