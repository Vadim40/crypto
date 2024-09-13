package com.example.wallet.Services.Interfaces;

import java.time.LocalDate;

public interface EventIdHashService {
    boolean checkAndSaveEvent(Object object);
    void deleteAllBeforeDate(LocalDate date);
    void deleteAllOldEvents();
}
