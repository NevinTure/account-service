package com.service.account.repositories;

import com.service.account.models.LogMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMessageRepository extends JpaRepository<LogMessage, Long> {
}
