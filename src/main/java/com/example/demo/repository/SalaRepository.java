package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Sala.SalaBasicProjection;
import com.example.demo.dto.Sala.SalaProjection;
import com.example.demo.model.Sala;
import com.example.demo.user.entity.User;


@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {
    List<SalaBasicProjection> findByOwner(User owner);
    Optional<SalaProjection> findProjectedById(Integer id);
    @Query("SELECT s FROM Sala s LEFT JOIN FETCH s.users WHERE s.id = :id")
    Optional<Sala> findByIdWithUsers(@Param("id") Integer id);
}
