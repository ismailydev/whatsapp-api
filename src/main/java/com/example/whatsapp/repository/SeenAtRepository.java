package com.example.whatsapp.repository;

import com.example.whatsapp.model.SeenAt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeenAtRepository extends JpaRepository<SeenAt, Long> {
}
