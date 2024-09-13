package com.example.wallet.Services;

import com.example.wallet.Models.EventIdHash;
import com.example.wallet.Repositories.EventIdHashRepository;
import com.example.wallet.Services.Interfaces.EventIdHashService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class EventIdHashServiceImpl implements EventIdHashService {
    private final EventIdHashRepository eventIdHashRepository;


    @Override
    public boolean checkAndSaveEvent(Object object) {
        String objectToString=object.toString();
        String hash=hash(objectToString);
        if(eventIdHashRepository.existsEventIdHashByHashId(hash)){
           return false;
        }
        saveEventIdHash(hash);
        return true;

    }

    private void saveEventIdHash(String hash) {
        EventIdHash newEventIdHash = new EventIdHash();
        newEventIdHash.setCreatedAt(LocalDate.now());
        newEventIdHash.setHashId(hash);
        eventIdHashRepository.save(newEventIdHash);
    }
    private String hash(String string) {
        return DigestUtils.sha256Hex(string);
    }

    @Override
    @Transactional
    public void deleteAllBeforeDate(LocalDate date) {
        eventIdHashRepository.deleteAllByCreatedAtBefore(date);
    }
    @Scheduled(initialDelay = 0, fixedRate = 3600000)
    @Override
    @Transactional
    public void deleteAllOldEvents() {
        LocalDate dateBefore= LocalDate.now();
    deleteAllBeforeDate(dateBefore);
    }
}
