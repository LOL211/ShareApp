package org.kush.share.api.controller.services;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.database.models.User;
import org.kush.share.api.database.models.UserList;
import org.kush.share.api.database.repository.ListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService
{
    private final ListRepository listRepository;

    public List<UserListDto> getList()
    {
        User u = new User();
        u.setId(1);
        List<UserList> lists = listRepository.findAllListsOfAUser(u);

        return lists.stream().map(list ->
            new UserListDto(list.getListName(), list.getCreatedBy().getUsername(), null)
        ).toList();
    }
}
