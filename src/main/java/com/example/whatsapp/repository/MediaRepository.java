package com.example.whatsapp.repository;

import com.example.whatsapp.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Media findFirstByLocation(String location);
}
