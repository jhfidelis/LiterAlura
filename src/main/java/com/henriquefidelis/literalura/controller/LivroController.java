package com.henriquefidelis.literalura.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henriquefidelis.literalura.service.ConsumoAPI;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @GetMapping
    public String listarLivros() {
        ConsumoAPI consumo = new ConsumoAPI();
        String json = consumo.obterDados("https://gutendex.com/books?search=dom%20casmurro");
        return json;
    }
    
}
