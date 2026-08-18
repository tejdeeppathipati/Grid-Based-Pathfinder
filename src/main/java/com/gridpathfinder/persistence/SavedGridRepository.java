package com.gridpathfinder.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SavedGridRepository extends JpaRepository<SavedGrid, UUID> {
}
