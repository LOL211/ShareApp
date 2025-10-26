package org.kush.share.api;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.database.models.ListItem;
import org.kush.share.api.database.models.User;
import org.kush.share.api.database.models.UserList;
import org.kush.share.api.database.repository.ItemsRepository;
import org.kush.share.api.database.repository.ListRepository;
import org.kush.share.api.database.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("local")
public class TestRunner implements CommandLineRunner
{
    private final ListRepository listRepository;
    private final UserRepository userRepository;
    private final ItemsRepository itemsRepository;

    @Override
    public void run(String... args) throws Exception
    {
        User u = new User();
        u.setUsername("admin");
        u.setEmail("test");

        u = userRepository.save(u);
        UserList userList = new UserList();
        userList.setCreatedBy(u);
        userList.setListName("my new list");
        userList = listRepository.save(userList);

        ListItem listItem = new ListItem();

        listItem.setUserList(userList);
        listItem.setLink("https://www.google.com");
        listItem.setDescription("this is a test");

        listItem = itemsRepository.save(listItem);
    }
}
