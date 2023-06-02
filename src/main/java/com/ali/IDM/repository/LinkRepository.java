package com.ali.IDM.repository;

import com.ali.IDM.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LinkRepository extends JpaRepository<Link, UUID> {
}
