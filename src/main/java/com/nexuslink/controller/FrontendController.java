package com.nexuslink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Este Controller tem a responsabilidade de redirecionar todas as rotas
 * do frontend para o index.html, permitindo que o React Router
 * controle a navegação no lado do cliente.
 */
@Controller
public class FrontendController {

    @RequestMapping(value = {"/", "/signup", "/dashboard", "/dashboard/{path:^(?!api$).*$}/**"})
    public String forward() {
        // A palavra "forward:" transfere a requisição internamente para o index.html,
        // mantendo a URL original no navegador.
        return "forward:/index.html";
    }
}