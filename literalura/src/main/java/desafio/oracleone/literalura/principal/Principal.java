package desafio.oracleone.literalura.principal;

import com.fasterxml.jackson.databind.JsonNode;
import desafio.oracleone.literalura.model.Autor;
import desafio.oracleone.literalura.model.Livro;
import desafio.oracleone.literalura.model.LivroDTO;
import desafio.oracleone.literalura.respository.LivroRepository;
import desafio.oracleone.literalura.service.ConsumoAPI;
import desafio.oracleone.literalura.service.ConverteDados;
import desafio.oracleone.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroService livroService;


    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConverteDados converteDados;

    private void salvarLivros(List<Livro> livros) {
        livros.forEach(livroRepository::save);
    }


    private final Scanner leitura = new Scanner(System.in);

    public Principal(LivroRepository livroRepository, ConsumoAPI consumoAPI, ConverteDados converteDados) {
        this.livroRepository = livroRepository;
        this.consumoAPI = consumoAPI;
        this.converteDados = converteDados;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            exibirMenu();

            String entrada = leitura.nextLine();

            int opcao;
            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida! Por favor, digite um número.");
                continue;
            }

            switch (opcao) {
                case 1 -> buscarLivrosPeloTitulo();
                case 2 -> buscarLivrosPorAutor();
                case 3 -> listarLivrosRegistrados();
                case 4 -> listarLivrosPorIdioma();
                case 5 -> listarAutoresVivos();
                case 6 -> listarAutoresNascidos();
                case 7 -> listarAutoresPorAnoDeMorte();
                case 8 -> listarAutoresRegistrados();
                case 9 -> top10LivrosMaisBaixados();
                case 0 -> {
                    System.out.println("Encerrando a LiterAlura!");
                    running = false;
                }
                default -> System.out.println("Opção inválida! Por favor, digite um número válido.");
            }
        }
    }


    private void exibirMenu() {
        System.out.println("""
        ========================== DESAFIO LITERALURA ==========================
        
        📋 Menu de Opções:
        
        [1] Buscar livros pelo título
        [2] Buscar livro por nome do autor
        [3] Listar livros já registrados
        [4] Listar livros em determinado idioma
        [5] Listar autores VIVOS em um determinado ano
        [6] Listar autores NASCIDOS em determinado ano
        [7] Listar autores por ano de sua morte
        [8] Listar todos os autores já registrados
        [9] Top 10 livros mais baixados!!!
        [0] Sair
        
        =========================================================================
        Escolha uma opção:
        """);

    }

    private void buscarLivrosPeloTitulo() {
        String baseURL = "https://gutendex.com/books?search=";

        try {
            System.out.println("Digite o título do livro: ");
            String titulo = leitura.nextLine();
            String endereco = baseURL + titulo.replace(" ", "%20");

            String jsonResponse = consumoAPI.obterDados(endereco);

            if (jsonResponse.isEmpty()) {
                System.out.println("Resposta da API está vazia.");
                return;
            }

            JsonNode rootNode = converteDados.getObjectMapper().readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");

            if (resultsNode.isEmpty()) {
                System.out.println("Não foi possível encontrar o livro buscado.");
                return;
            }

            List<LivroDTO> livrosDTO = converteDados.getObjectMapper()
                    .readerForListOf(LivroDTO.class)
                    .readValue(resultsNode);

            List<Livro> livrosExistentes = livroRepository.findByTitulo(titulo);
            if (!livrosExistentes.isEmpty()) {
                livrosDTO.removeIf(livroDTO -> livrosExistentes.stream()
                        .anyMatch(livroExistente -> livroExistente.getTitulo().equals(livroDTO.titulo())));
            }

            if (!livrosDTO.isEmpty()) {
                List<Livro> novosLivros = livrosDTO.stream().map(Livro::new).collect(Collectors.toList());
                salvarLivros(novosLivros);
                System.out.println("Livros salvos com sucesso!");
            }

            livrosDTO.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Erro ao buscar livros: " + e.getMessage());
        }
    }

    private void buscarLivrosPorAutor() {
        String baseURL = "https://gutendex.com/books?";

        try {
            System.out.println("Digite o nome do autor: ");
            String nomeAutor = leitura.nextLine();
            String endereco = baseURL + "search=" + URLEncoder.encode(nomeAutor, StandardCharsets.UTF_8);

            List<LivroDTO> livrosDTO = new ArrayList<>();
            String nextPage = endereco;

            do {
                String jsonResponse = consumoAPI.obterDados(nextPage);

                if (jsonResponse.isEmpty()) {
                    System.out.println("Resposta da API está vazia.");
                    return;
                }

                JsonNode rootNode = converteDados.getObjectMapper().readTree(jsonResponse);
                JsonNode resultsNode = rootNode.path("results");

                List<LivroDTO> livrosPagina = converteDados.getObjectMapper()
                        .readerForListOf(LivroDTO.class)
                        .readValue(resultsNode);
                livrosDTO.addAll(livrosPagina);

                nextPage = rootNode.path("next").asText(null);

            } while (nextPage != null);

            List<Livro> novosLivros = livrosDTO.stream().map(Livro::new).collect(Collectors.toList());
            salvarLivros(novosLivros);

            livrosDTO.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Erro ao buscar livros por autor: " + e.getMessage());
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("""
                Digite o idioma pretendido:
                Português (pt)
                Alemão (de)
                Espanhol (es)
                Francês (fr)
                Inglês (en)
                Japonês (ja)
                """);
        String idioma = leitura.nextLine();

        List<Livro> livros = livroRepository.findByIdioma(idioma);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma especificado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Digite o ano: ");
        Integer ano = leitura.nextInt();
        leitura.nextLine();

        Year year = Year.of(ano);

        List<Autor> autores = livroRepository.findAutoresVivos(year);
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado no ano informado.");
        } else {
            System.out.println("Lista de autores vivos no ano de " + ano + ":\n");

            Set<String> nomesExibidos = new HashSet<>();

            autores.forEach(autor -> {
                if (nomesExibidos.add(autor.getAutor())) {
                    String nomeAutor = autor.getAutor();
                    String anoNascimento = autor.getAnoNascimento().toString();
                    String anoFalecimento = autor.getAnoFalecimento() != null ? autor.getAnoFalecimento().toString() : "Vivo";
                    System.out.println(nomeAutor + " (" + anoNascimento + " - " + anoFalecimento + ")");
                }
            });
        }
    }


    private void listarAutoresNascidos() {
        System.out.println("Digite o ano: ");
        Integer ano = leitura.nextInt();
        leitura.nextLine();

        Year year = Year.of(ano);

        List<Autor> autores = livroRepository.findAutoresVivosRefinado(year);
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor nascido no ano informado.");
        } else {
            System.out.println("Lista de autores nascidos no ano de " + ano + ":\n");

            Set<String> nomesExibidos = new HashSet<>();

            autores.forEach(autor -> {
                if (nomesExibidos.add(autor.getAutor())) {
                    String nomeAutor = autor.getAutor();
                    String anoNascimento = autor.getAnoNascimento().toString();
                    String anoFalecimento = autor.getAnoFalecimento() != null ? autor.getAnoFalecimento().toString() : "Vivo";
                    System.out.println(nomeAutor + " (" + anoNascimento + " - " + anoFalecimento + ")");
                }
            });
        }
    }


    private void listarAutoresPorAnoDeMorte() {
        System.out.println("Digite o ano: ");
        Integer ano = leitura.nextInt();
        leitura.nextLine();

        Year year = Year.of(ano);

        Set<String> autores = livroRepository.findAutoresPorAnoDeMorte(year).stream()
                .map(autor -> autor.getAutor() + " (" + autor.getAnoNascimento() + " - " + autor.getAnoFalecimento() + ")")
                .collect(Collectors.toSet());

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado que morreu no ano de " + ano);
        } else {
            System.out.println("Lista de autores que morreram no ano de " + ano + ":\n");
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        Set<String> autores = livroRepository.findAll().stream()
                .map(livro -> livro.getAutor().getAutor())
                .collect(Collectors.toSet());

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            System.out.println("Autores registrados:");
            autores.forEach(System.out::println);
        }
    }

    private void top10LivrosMaisBaixados() {
        List<Livro> livros = livroService.top10LivrosMaisBaixados();
        if (livros.isEmpty()) {
            System.out.println("Não há livros cadastrados.");
        } else {
            System.out.println("Top 10 livros mais baixados:");
            livros.forEach(livro -> System.out.println(
                    "Título: " + livro.getTitulo() + ", Downloads: " + livro.getNumeroDownloads()));
        }
    }

}

