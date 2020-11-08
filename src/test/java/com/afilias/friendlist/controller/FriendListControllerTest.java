package com.afilias.friendlist.controller;

import com.afilias.friendlist.exception.ResourceNotFoundException;
import com.afilias.friendlist.model.dto.UserDTO;
import com.afilias.friendlist.service.FriendListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FriendListControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private FriendListService friendListService;

    @InjectMocks
    private FriendListController friendListController;

    @InjectMocks
    private MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(friendListController).build();
    }

    @Test
    public void getUserList_Successful() {
        // Given
        List<UserDTO> serviceResult = Arrays.asList(UserDTO.builder().id(1).name("John").city("Galway").build(),
                UserDTO.builder().id(2).name("Sean").city("Cork").build());

        // When
        Mockito.when(friendListService.getUserList()).thenReturn(serviceResult);

        // Then
        ResponseEntity<List<UserDTO>> result = friendListController.getUserList(request);

        // Assertions
        assertNotNull(result);
        UserDTO user_1 = result.getBody().get(0);
        assertEquals(1, user_1.getId());
        assertEquals("John", user_1.getName());
        assertEquals("Galway", user_1.getCity());
        UserDTO user_2 = result.getBody().get(1);
        assertEquals(2, user_2.getId());
        assertEquals("Sean", user_2.getName());
        assertEquals("Cork", user_2.getCity());

        // verification
        verify(friendListService).getUserList();
    }

    @Test
    public void getUserFriendList_ResourceNotFound() throws ResourceNotFoundException {

        // When
        Mockito.when(friendListService.getUserFriendList(1)).thenThrow(ResourceNotFoundException.class);

        // Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            friendListController.getUserFriendList(request, 1);
        });

        // verification
        verify(friendListService).getUserFriendList(1);
    }

    @Test
    public void getUserFriendList_ArgumentTypeMisMatchException() {
        //Then
        Assertions.assertThrows(NumberFormatException.class, () ->
        {
            friendListController.getUserFriendList(request, Long.valueOf("asd"));
        });
    }

    // TODO: Add rest of junit tests for methods getUserSuggestedFriendList & getUserSuggestedFriendGeoList
}
