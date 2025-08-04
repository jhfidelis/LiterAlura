package com.henriquefidelis.literalura.main;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import com.henriquefidelis.literalura.models.DadosLivro;
import com.henriquefidelis.literalura.models.DadosResposta;
import com.henriquefidelis.literalura.service.ConsumoAPI;
import com.henriquefidelis.literalura.service.ConverteDados;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public final String ENDERECO = "https://gutendex.com/books/?search=";

    public void exibirMenu() {
        var opcao = -1;

        while (opcao != 0) {
            System.out.println("==============================================");
            var menu = """
                    1 - Buscar livro pelo tírulo
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
        System.out.println("JSON retornado:\n" + json);
        DadosResposta dados = conversor.obterDados(json, DadosResposta.class);

        List<DadosLivro> livros = dados.livros();
        if (livros == null || livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

}
