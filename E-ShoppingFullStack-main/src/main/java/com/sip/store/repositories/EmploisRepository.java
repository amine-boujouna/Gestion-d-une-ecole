package com.sip.store.repositories;

import com.sip.store.entities.Emplois;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploisRepository extends JpaRepository<Emplois,Long> {


}
