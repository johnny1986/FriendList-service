package com.afilias.friendlist.service;

import com.afilias.friendlist.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FriendListServiceTest {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private FriendListService friendListService;

    @InjectMocks
    private ModelMapper modelMapper;

}
