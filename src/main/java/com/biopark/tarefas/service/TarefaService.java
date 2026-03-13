package com.biopark.tarefas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.biopark.tarefas.model.Tarefa;
import com.biopark.tarefas.model.TarefaStatus;
import com.biopark.tarefas.repository.TarefaRepository;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public Tarefa salvar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<Tarefa> listarPendentes() {
        return tarefaRepository.findByStatus(TarefaStatus.PENDING);
    }

    public List<Tarefa> listarConcluidas() {
        return tarefaRepository.findByStatus(TarefaStatus.FINISHED);
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    public void concluirTarefa(Long id) {
        Optional<Tarefa> currentlyTask = tarefaRepository.findById(id);
        currentlyTask.ifPresent(tarefaRepository::concluirTarefa);
    }

    public void excluir(Long id) {
        tarefaRepository.deleteById(id);
    }
    
}
