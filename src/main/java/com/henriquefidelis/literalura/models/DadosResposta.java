package com.henriquefidelis.literalura.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosResposta(
    @JsonAlias("results") List<DadosLivro> livros
) {

}
