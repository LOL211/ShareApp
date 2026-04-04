package org.kush.share.api.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kush.share.api.database.repository.ShareRequestRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CleanupShareRequestsTest
{
    @Mock
    private ShareRequestRepository shareRequestRepository;

    @InjectMocks
    private CleanupShareRequests cleanupShareRequests;

    /**
     * Calling cleanup() directly will fail to compile if the method is private,
     * which would have caught that bug immediately.
     */
    @Test
    void cleanup_fetchesInvalidIdsAndDeletesThem()
    {
        List<UUID> staleIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(shareRequestRepository.findAllInvalidShareRequests()).thenReturn(staleIds);

        cleanupShareRequests.cleanup();

        verify(shareRequestRepository).findAllInvalidShareRequests();
        verify(shareRequestRepository).deleteAllById(staleIds);
    }

    @Test
    void cleanup_doesNothingWhenThereAreNoInvalidRequests()
    {
        when(shareRequestRepository.findAllInvalidShareRequests()).thenReturn(Collections.emptyList());

        cleanupShareRequests.cleanup();

        verify(shareRequestRepository).deleteAllById(Collections.emptyList());
    }

    /**
     * Verifies cleanup() is public AND carries the @Scheduled cron.
     * getMethod() only finds public methods, so a private cleanup() would throw
     * NoSuchMethodException and fail the test.
     */
    @Test
    void cleanup_isPublicAndAnnotatedWithExpectedCron() throws NoSuchMethodException
    {
        Method method = CleanupShareRequests.class.getMethod("cleanup");

        Scheduled scheduled = method.getAnnotation(Scheduled.class);
        assertNotNull(scheduled, "cleanup() must be annotated with @Scheduled");
        assertEquals("0 0 0 * * *", scheduled.cron());
    }
}

