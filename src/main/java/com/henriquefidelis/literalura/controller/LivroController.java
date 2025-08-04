package com.henriquefidelis.literalura.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henriquefidelis.literalura.models.DadosLivro;
import com.henriquefidelis.literalura.models.DadosResposta;
import com.henriquefidelis.literalura.service.ConsumoAPI;
import com.henriquefidelis.literalura.service.ConverteDados;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @GetMapping
    public DadosLivro listarLivros() {
        ConsumoAPI consumo = new ConsumoAPI();
        String json = consumo.obterDados("https://gutendex.com/books?search=dom%20casmurro");
        System.out.println(json);

        ConverteDados conversor = new ConverteDados();
        DadosResposta dados = conversor.obterDados(json, DadosResposta.class);
        System.out.println(dados);

        return dados.results().get(0);
    }
    
}
