package br.com.mikaelson.regestweb.repositories;

import br.com.mikaelson.regestweb.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository <Professor, Long> {
}
