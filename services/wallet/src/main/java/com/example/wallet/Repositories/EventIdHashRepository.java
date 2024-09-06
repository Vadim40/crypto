package com.example.wallet.Repositories;

import com.example.wallet.Models.EventIdHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EventIdHashRepository extends JpaRepository<EventIdHash, Long> {
    boolean existsEventIdHashByHashId(String hashId);
    void deleteAllByCreatedAtBefore(LocalDate localDate);
}
