package com.afilias.friendlist.controller;

import com.afilias.friendlist.exception.ResourceNotFoundException;
import com.afilias.friendlist.model.dto.UserDTO;
import com.afilias.friendlist.model.dto.UserWithFriendsDTO;
import com.afilias.friendlist.service.FriendListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "FriendListService")
@Slf4j
public class FriendListController {

    private FriendListService friendListService;

    @Autowired
    public FriendListController(FriendListService friendListService) {
        this.friendListService = friendListService;
    }

    @ApiOperation(value = "Get user's list", response = List.class)
    @GetMapping(path = "/getUserList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserList(final HttpServletRequest request) {
        return ResponseEntity.ok(friendListService.getUserList());
    }

    @ApiOperation(value = "Get user's friend list", response = List.class)
    @GetMapping(path = "/getUserFriendList/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserFriendList(final HttpServletRequest request, @PathVariable long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(friendListService.getUserFriendList(userId));
    }

    @ApiOperation(value = "Get user's suggested friend list (friend of a friend)", response = List.class)
    @GetMapping(path = "/getUserSuggestedFriendList/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserWithFriendsDTO>> getUserSuggestedFriendList(final HttpServletRequest request, @PathVariable long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(friendListService.getUserSuggestedFriendList(userId));
    }

    @ApiOperation(value = "Get user's suggested friend list sorted geographically", response = List.class)
    @GetMapping(path = "/getUserSuggestedFriendGeoList/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserSuggestedFriendGeoList(final HttpServletRequest request, @PathVariable long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(friendListService.getUserSuggestedFriendGeoList(userId));
    }

}
