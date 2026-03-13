package com.biopark.tarefas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biopark.tarefas.model.Tarefa;
import com.biopark.tarefas.service.TarefaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tarefas", tarefaService.listarTodas());
        return "tarefas/lista";
    }

    @GetMapping("/pendentes")
    public String listarPendentes(Model model) {
        model.addAttribute("tarefas", tarefaService.listarPendentes());
        return "tarefas/lista";
    }

    @GetMapping("/concluidas")
    public String listarConcluidas(Model model) {
        model.addAttribute("tarefas", tarefaService.listarConcluidas());
        return "tarefas/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("tarefa", new Tarefa());
        return "tarefas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return tarefaService.buscarPorId(id)
                .map(tarefa -> {
                    model.addAttribute("tarefa", tarefa);
                    return "tarefas/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensagemErro", "Tarefa não encontrada!");
                    return "redirect:/tarefas";
                });
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Tarefa tarefa, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tarefas/formulario";
        }

        boolean isNova = tarefa.getId() == null;

        // Se estiver editando, preservar a data de criação original
        if (!isNova) {
            tarefaService.buscarPorId(tarefa.getId()).ifPresent(existente -> {
                tarefa.setDataCriacao(existente.getDataCriacao());
            });
        }

        tarefaService.salvar(tarefa);

        if (isNova) {
            redirectAttributes.addFlashAttribute("mensagem", "Tarefa criada!");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Tarefa atualizada!");
        }

        return "redirect:/tarefas";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tarefaService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Tarefa excluída!");
        return "redirect:/tarefas";
    }

    @GetMapping("/status/{id}")
    public String alternarStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tarefaService.concluirTarefa(id);
        redirectAttributes.addFlashAttribute("mensagem", "Status da tarefa alterado!");
        return "redirect:/tarefas";
    }
}
