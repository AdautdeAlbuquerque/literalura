package desafio.oracleone.literalura.service;

import desafio.oracleone.literalura.model.Autor;
import desafio.oracleone.literalura.model.Livro;
import desafio.oracleone.literalura.model.LivroDTO;
import desafio.oracleone.literalura.respository.LivroRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> top10LivrosMaisBaixados() {
        return livroRepository.findTop10ByOrderByNumeroDownloadsDesc();
    }
}
