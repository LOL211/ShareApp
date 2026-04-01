package org.kush.share.api.controller.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kush.share.api.database.models.UserList;
import org.kush.share.api.database.repository.ItemsRepository;
import org.kush.share.api.database.repository.ListRepository;
import org.kush.share.api.database.repository.ShareRequestRepository;
import org.kush.share.api.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListServiceTest
{
    @Mock
    private ListRepository listRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemsRepository itemsRepository;

    @Mock
    private ShareRequestRepository shareRequestRepository;

    @InjectMocks
    private ListService listService;

    @Test
    void deleteList_whenUserIsOwner_deletesTheList()
    {
        UUID userId = UUID.randomUUID();
        String listName = "myList";
        UserList userList = new UserList();
        userList.setCreatedBy(userId);
        userList.setListName(listName);
        userList.setSharedWith(new HashSet<>());

        when(listRepository.findAllListsOfAUser(userId)).thenReturn(List.of(userList));

        listService.deleteList(userId.toString(), listName);

        verify(listRepository).delete(userList);
        verify(listRepository, never()).save(any());
    }

    @Test
    void deleteList_whenListIsSharedToUser_removesUserFromSharedWith()
    {
        UUID ownerId = UUID.randomUUID();
        UUID sharedUserId = UUID.randomUUID();
        String listName = "sharedList";
        UserList userList = new UserList();
        userList.setCreatedBy(ownerId);
        userList.setListName(listName);
        Set<UUID> sharedWith = new HashSet<>();
        sharedWith.add(sharedUserId);
        userList.setSharedWith(sharedWith);

        when(listRepository.findAllListsOfAUser(sharedUserId)).thenReturn(List.of(userList));

        listService.deleteList(sharedUserId.toString(), listName);

        assertFalse(userList.getSharedWith().contains(sharedUserId));
        verify(listRepository).save(userList);
        verify(listRepository, never()).delete(any(UserList.class));
    }

    @Test
    void deleteList_whenListNotFound_throwsIllegalArgumentException()
    {
        UUID userId = UUID.randomUUID();

        when(listRepository.findAllListsOfAUser(userId)).thenReturn(Collections.emptyList());

        assertThrows(IllegalArgumentException.class,
                () -> listService.deleteList(userId.toString(), "nonExistentList"));
    }

    @Test
    void deleteList_whenListNotSharedWithUser_throwsIllegalArgumentException()
    {
        UUID ownerId = UUID.randomUUID();
        UUID unrelatedUserId = UUID.randomUUID();
        String listName = "someList";
        UserList userList = new UserList();
        userList.setCreatedBy(ownerId);
        userList.setListName(listName);
        userList.setSharedWith(new HashSet<>());

        when(listRepository.findAllListsOfAUser(unrelatedUserId)).thenReturn(List.of(userList));

        assertThrows(IllegalArgumentException.class,
                () -> listService.deleteList(unrelatedUserId.toString(), listName));

        verify(listRepository, never()).save(any());
        verify(listRepository, never()).delete(any(UserList.class));
    }
}
