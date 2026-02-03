package org.example.web_springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/")
class WebController {

    GameRepository gameRepository;
    public WebController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String platform,Model model)
    {
        model.addAttribute("platforms", gameRepository.findAllPlatforms());
        if (platform != null && !platform.isEmpty()){
            model.addAttribute("games", gameRepository.findGamesByPlatformOrderByYearDesc(platform));
        }else {
            model.addAttribute("games", gameRepository.findAll());
        }
        return "index";
    }

    @GetMapping("/juego/{id}")
    public String juego(@PathVariable Integer id, Model model)
    {
        model.addAttribute("platforms", gameRepository.findAllPlatforms());
        Optional<Game> gameOptional = gameRepository.findById(id);
        if (gameOptional.isPresent()) {
            model.addAttribute("game", gameOptional.get());
            return "juego";
        } else {
            model.addAttribute("error", "No se ha encontrado el juego con ID: " + id);
            return "error";
        }
    }

    @GetMapping("/juego/nuevo")
    public String nuevoJuegoForm(Model model) {
        model.addAttribute("game", new Game());
        return "form";
    }

    @PostMapping("/juego/nuevo")
    public String nuevoJuego(Game game) {
        gameRepository.save(game);
        return "redirect:/";
    }

    @GetMapping("/juego/editar/{id}")
    public String editarJuegoForm(@PathVariable Integer id, Model model) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        if (gameOptional.isPresent()) {
            model.addAttribute("game", gameOptional.get());
            return "form";
        } else {
            model.addAttribute("error", "No se ha encontrado el juego con ID: " + id);
            return "error";
        }
    }

    @PostMapping("/juego/editar/{id}")
    public String editarJuego(Game game) {
        gameRepository.save(game);
        return "redirect:/";
    }
}
