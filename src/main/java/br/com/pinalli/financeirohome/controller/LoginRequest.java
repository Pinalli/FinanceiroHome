package br.com.pinalli.financeirohome.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class LoginRequest {

    private String nome;
    private String email;
    private String senha;


}
