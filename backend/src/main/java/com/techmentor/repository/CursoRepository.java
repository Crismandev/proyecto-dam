package com.techmentor.repository;

import com.techmentor.entity.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Query("SELECT c FROM Curso c WHERE (:idCategoria IS NULL OR c.categoria.idCategoria = :idCategoria) AND (LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(COALESCE(c.descripcion, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Curso> buscar(@Param("idCategoria") Long idCategoria, @Param("q") String q, Pageable pageable);

    @Query("SELECT c FROM Curso c WHERE (:idCategoria IS NULL OR c.categoria.idCategoria = :idCategoria) AND (LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(COALESCE(c.descripcion, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Curso> buscarTodosFiltro(@Param("idCategoria") Long idCategoria, @Param("q") String q);
}
