package desafio.oracleone.literalura.respository;

import desafio.oracleone.literalura.model.Autor;
import desafio.oracleone.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    boolean existsByTitulo(String titulo);

    @Query("SELECT l FROM Livro l WHERE LOWER(l.titulo) = LOWER(:titulo)")
    List<Livro> findByTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Livro l WHERE LOWER(l.autor.autor) LIKE LOWER(CONCAT('%', :nomeAutor, '%'))")
    List<Livro> findByAutorNome(@Param("nomeAutor") String nomeAutor);

    @Query("SELECT l.autor FROM Livro l WHERE l.autor.anoNascimento <= :ano AND (l.autor.anoFalecimento IS NULL OR l.autor.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivos(@Param("ano") Year ano);

    @Query("SELECT l.autor FROM Livro l WHERE l.autor.anoNascimento = :ano AND (l.autor.anoFalecimento IS NULL OR l.autor.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivosRefinado(@Param("ano") Year ano);

    @Query("SELECT l.autor FROM Livro l WHERE l.autor.anoNascimento <= :ano AND l.autor.anoFalecimento = :ano")
    List<Autor> findAutoresPorAnoDeMorte(@Param("ano") Year ano);

    @Query("SELECT l FROM Livro l WHERE l.idioma LIKE %:idioma%")
    List<Livro> findByIdioma(@Param("idioma") String idioma);

    @Query("SELECT l.autor FROM Livro l WHERE LOWER(l.autor.autor) = LOWER(:nomeAutor)")
    Optional<Autor> findAutorByNome(@Param("nomeAutor") String nomeAutor);

    List<Livro> findTop10ByOrderByNumeroDownloadsDesc();



}
