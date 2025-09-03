package siacor.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpController {
    @GetMapping("/obtenerIPCliente")
    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr(); // Obtiene la IP del cliente
        return clientIp;
    }
}
