package com.henriquefidelis.literalura.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosResposta(
    List<DadosLivro> results
) {

}
