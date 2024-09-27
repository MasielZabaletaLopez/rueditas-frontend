package com.example.Rueditas_frontend.Controller;

import com.example.Rueditas_frontend.dto.RueditasRequestDTO;
import com.example.Rueditas_frontend.dto.RueditasResponseDTO;
import com.example.Rueditas_frontend.viewModel.RueditasModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/buscar")
public class RueditasController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        RueditasModel rueditasModel = new RueditasModel("00", "");
        model.addAttribute("rueditasModel", rueditasModel);
        return "inicio";
    }

    @PostMapping("/placa")
    public String buscarPlaca(@RequestParam("placa") String placa, Model model) {
        if (placa == null || placa.trim().length() == 0 || placa.trim().length() != 7 ||
                !placa.matches("^[A-Za-z0-9-]+$")) {
            RueditasModel rueditasModel = new RueditasModel("01", "Error: Debe ingresar una placa correcta");
            model.addAttribute("rueditasModel", rueditasModel);
            return "inicio";
        }

        try {
            String endpoint = "http://localhost:8080/buscar/placa";

            RueditasRequestDTO rueditasRequest = new RueditasRequestDTO(placa);
            RueditasResponseDTO rueditasResponse = restTemplate.postForObject(endpoint, rueditasRequest, RueditasResponseDTO.class);

            if(rueditasResponse.codigo().equals("00")) {
                RueditasModel rueditasModel = new RueditasModel("05", "");
                model.addAttribute("rueditasModel", rueditasModel);
                model.addAttribute("rueditasResponse", rueditasResponse);
                return "principal";
            }else {
                RueditasModel rueditasModel = new RueditasModel("03", "Busqueda fallida");
                model.addAttribute("rueditasModel", rueditasModel);
                return "inicio";
            }
        } catch (Exception e) {
            RueditasModel rueditasModel = new RueditasModel("10", "Ocurrio un problema en la busqueda");
            model.addAttribute("rueditasModel", rueditasModel);
            return "inicio";
        }
    }
}