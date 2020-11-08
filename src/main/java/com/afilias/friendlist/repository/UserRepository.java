package com.afilias.friendlist.repository;

import com.afilias.friendlist.model.entity.User;
import com.afilias.friendlist.model.view.UserWithFriendsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "Select id, name, city, friendid, friendname, friendcity from user_with_friends where id = :id", nativeQuery = true)
    List<UserWithFriendsView> getAllFriendsAndUserByUserId(long id);

    @Query(value = "Select friendid, friendname, friendcity from user_with_friends where id = :id", nativeQuery = true)
    List<UserWithFriendsView> getAllFriendsByUserId(long id);

    @Query(value = "Select id, name, city, friendid, friendname, friendcity from user_with_friends where id in (:idList) order by id, friendid", nativeQuery = true)
    List<UserWithFriendsView> getAllFriendsByUserListId(List<Long> idList);

}
