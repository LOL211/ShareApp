package org.kush.share.api.job;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.database.repository.ShareRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CleanupShareRequests
{
    private final ShareRequestRepository shareRequestRepository;

    @Scheduled(cron = "0 0 0 * * *")
    private void cleanup()
    {
        //Leaving this here so later logging can be added
       List<UUID> uuidList = shareRequestRepository.findAllInvalidShareRequests();

       shareRequestRepository.deleteAllById(uuidList);
    }
}
