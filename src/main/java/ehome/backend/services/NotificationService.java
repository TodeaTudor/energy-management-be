package ehome.backend.services;

import ehome.backend.dtos.WarningMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate template;

    @Autowired
    public NotificationService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notify(WarningMessageDTO warningMessageDTO, String username) {
        template.convertAndSendToUser(username, "/topic/message", warningMessageDTO);
    }
}
