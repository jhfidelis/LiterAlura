package com.henriquefidelis.literalura.main;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.henriquefidelis.literalura.models.DadosAutor;
import com.henriquefidelis.literalura.models.DadosLivro;
import com.henriquefidelis.literalura.models.DadosResposta;
import com.henriquefidelis.literalura.service.ConsumoAPI;
import com.henriquefidelis.literalura.service.ConverteDados;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public final String ENDERECO = "https://gutendex.com/books/?search=";

    private List<DadosLivro> dadosLivros = new ArrayList<>();

    public void exibirMenu() {
        var opcao = -1;

        while (opcao != 0) {
            System.out.println("==============================================");
            var menu = """
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma

                    0 - Sair
                    """;

            System.out.print(menu + "\nEscolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarDadosLivro();
                    break;
                case 2:
                    listarLivrosBuscados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAno();
                    break;
                case 5:
                    listarLivrosBuscadosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarDadosLivro() {
        System.out.print("Digite o nome de um livro: ");
        var nomeLivro = sc.nextLine();

        var nomeCodificado = URLEncoder.encode(nomeLivro, StandardCharsets.UTF_8)
                .replace("+", "%20");
        var json = consumoAPI.obterDados(ENDERECO + nomeCodificado);
        DadosResposta dados = conversor.obterDados(json, DadosResposta.class);

        List<DadosLivro> livros = dados.livros();
        if (livros == null || livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            dadosLivros.addAll(livros);
            livros.forEach(System.out::println);
        }
    }

    private void listarLivrosBuscados() {
        if (dadosLivros.isEmpty()) {
            System.out.println("Ainda não há nenhum livro registrado");
        } else {
            dadosLivros.forEach(System.out::println);
        }
    }

    private void listarLivrosBuscadosPorIdioma() {
        var menuIdiomas = """
                \nes - espanhol
                en - inglês
                fr - francês
                pt - português

                Escolha o idioma para realizar a busca:
                """;
        System.out.print(menuIdiomas);
        var escolhaIdioma = sc.nextLine().toLowerCase();

        List<DadosLivro> livrosFiltrados = dadosLivros.stream()
                .filter(l -> l.idiomas() != null && l.idiomas().contains(escolhaIdioma))
                .toList();
        if (livrosFiltrados.isEmpty()) {
            System.out.println("Não foi encontrado nenhum livro no idioma escolhido");
        } else {
            System.out.println("LIVROS NO IDIOMA ESCOLHIDO");
            livrosFiltrados.forEach(System.out::println);
        }

    }

    public void listarAutoresRegistrados() {
        System.out.print("\nDigite o nome de um autor: ");
        var nomeAutor = sc.nextLine().toLowerCase();

        Optional<DadosAutor> autorEncontrado = dadosLivros.stream()
                .flatMap(l -> l.autores().stream())
                .filter(a -> a.nome().toLowerCase().contains(nomeAutor)).findFirst();

        if (autorEncontrado.isPresent()) {
            System.out.println(autorEncontrado.get().nome());
        } else {
            System.out.println("Nenhum autor foi encontrado");
        }
    }

    public void listarAutoresVivosPorAno() {
        System.out.print("Digite um ano para ser pesquisado: ");
        var anoPesquisado = sc.nextInt();
        sc.nextLine();

        List<DadosAutor> autoresVivos = dadosLivros.stream()
                .flatMap(l -> l.autores().stream()).distinct()
                .filter(a -> a.anoDeNascimento() != null
                        && a.anoDeNascimento() <= anoPesquisado
                        && (a.anoDeFalecimento() == null || a.anoDeFalecimento() > anoPesquisado))
                .toList();

        if (autoresVivos.isEmpty()) {
            System.out.println("Não foi encontrado autores vivos no ano de " + anoPesquisado);
        } else {
            System.out.println("Autores vivos em " + anoPesquisado);
            autoresVivos.forEach(System.out::println);
        }
    }

}
